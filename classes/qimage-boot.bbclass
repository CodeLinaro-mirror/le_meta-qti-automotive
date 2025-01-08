#Copyright (c) 2022-2023 Qualcomm Innovation Center, Inc. All rights reserved.
#SPDX-License-Identifier: BSD-3-Clause-Clear

DEPENDS += "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'qti-avb', 'avbtool-native', '', d)} \
    dtc-native \
    kernel-aosp-tools-native \
    mkdtimg-native \
    sectools-native \
    virtual/kernel \
    python3-native \
"

ghgvm_pilsplitter() {
    PILTOOLS_PATH="${STAGING_BINDIR_NATIVE}/scripts/pil_tools"

    DTB_FILE_LIST=$(find ${DEPLOY_DIR_IMAGE}/build-artifacts/dtb -name "*.dtb" | sort)
    if [ -z "${DTB_FILE_LIST}" ]; then
        echo "No *.dtb files found in $DEPLOY_DIR_IMAGE/dtbs"
        exit 1
    else
        ${STAGING_BINDIR_NATIVE}/build/prebuilts/kernel-build-tools/linux-x86/bin/mkdtimg create ${DEPLOY_DIR_IMAGE}/dtbs/dtb.img ${DEPLOY_DIR_IMAGE}/dtbs/lemans-gunyah-vm-lv-cob.dtb ${DEPLOY_DIR_IMAGE}/dtbs/lemans-gunyah-vm-lv-qam.dtb
    fi

    install -d ${DEPLOY_DIR_IMAGE}/signing

    # copy necessary files into signing dir
    cp ${DEPLOY_DIR_IMAGE}/Image ${DEPLOY_DIR_IMAGE}/signing
    cp ${DEPLOY_DIR_IMAGE}/ramdisk.img ${DEPLOY_DIR_IMAGE}/signing
    cp ${DEPLOY_DIR_IMAGE}/dtbs/dtb.img ${DEPLOY_DIR_IMAGE}/signing

    cd ${DEPLOY_DIR_IMAGE}/signing

    # autoghgvmlv-boot.elf is not a standard elf file, verify_elf in image_header.py
    # will fail and return 1.bypass yocto by adding 'set +e' and 'set -e'
    set +e
    python3 ${PILTOOLS_PATH}/image_header.py autoghgvmlv-boot.elf Image,0x0 dtb.img,0x3000000 ramdisk.img,0x3100000 --32
    set -e

    sectools secure-image autoghgvmlv-boot.elf --image-id GVM1 --security-profile ${STAGING_BINDIR_NATIVE}/${SECTOOLS_SECURITY_PROFILE} --sign --signing-mode TEST --outfile autoghgvmlv_signed-boot.elf
    sectools secure-image autoghgvmlv-boot.elf --inspect

    install -d ${DEPLOY_DIR_IMAGE}/signing/boot
    python3 ${PILTOOLS_PATH}/pil-splitter.py autoghgvmlv_signed-boot.elf boot/autoghgvmlv

    ${STAGING_BINDIR_NATIVE}/build/prebuilts/kernel-build-tools/linux-x86/bin/mkuserimg_mke2fs ${DEPLOY_DIR_IMAGE}/signing/boot ${DEPLOY_DIR_IMAGE}/${BOOTIMAGE_TARGET} ext4 / 60000000
}

do_ghgvm_pilsplitter() {
    touch ${DEPLOY_DIR_IMAGE}/ramdisk.img
    ghgvm_pilsplitter
}

do_ghgvm_pilsplitter[cleandirs] = "${DEPLOY_DIR_IMAGE}/signing"

python () {
    if bb.utils.contains('MACHINE_FEATURES', 'qti-ghgvm', True, False, d):
        bb.build.addtask('do_ghgvm_pilsplitter', 'do_sign_boot_img', 'do_makeboot', d)
}

do_merge_dtbs() {
     install -d ${DEPLOY_DIR_IMAGE}/build-artifacts/techpack-dtbs
     install -d ${DEPLOY_DIR_IMAGE}/dtbs

     if ${@oe.utils.version_less_or_equal('PREFERRED_VERSION_linux-msm', '6.0', 'true', 'false', d)}; then
         ${STAGING_BINDIR_NATIVE}/build/android/merge_dtbs.py \
         ${DEPLOY_DIR_IMAGE}/build-artifacts/dtb \
         ${DEPLOY_DIR_IMAGE}/build-artifacts/techpack-dtbs \
         ${DEPLOY_DIR_IMAGE}/dtbs

         if ${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', 'true', 'false', d)}; then
             install -d ${DEPLOY_DIR_IMAGE}/build-artifacts/techpack-dtbos
             install -d ${DEPLOY_DIR_IMAGE}/dtbos
             ${STAGING_BINDIR_NATIVE}/build/android/merge_dtbs.py \
             ${DEPLOY_DIR_IMAGE}/build-artifacts/dtbo \
             ${DEPLOY_DIR_IMAGE}/build-artifacts/techpack-dtbos \
             ${DEPLOY_DIR_IMAGE}/dtbos
         fi
     else
         ${STAGING_BINDIR_NATIVE}/build/android/merge_dtbs.py \
         --base ${DEPLOY_DIR_IMAGE}/build-artifacts/dtb \
         --techpack ${DEPLOY_DIR_IMAGE}/build-artifacts/techpack-dtbs \
         --out ${DEPLOY_DIR_IMAGE}/dtbs

         if ${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', 'true', 'false', d)}; then
             install -d ${DEPLOY_DIR_IMAGE}/build-artifacts/techpack-dtbos
             install -d ${DEPLOY_DIR_IMAGE}/dtbos
             ${STAGING_BINDIR_NATIVE}/build/android/merge_dtbs.py \
             --base ${DEPLOY_DIR_IMAGE}/build-artifacts/dtbo \
             --techpack ${DEPLOY_DIR_IMAGE}/build-artifacts/techpack-dtbos \
             --out ${DEPLOY_DIR_IMAGE}/dtbos
         fi
     fi

     cat ${DEPLOY_DIR_IMAGE}/dtbs/*.dtb > ${DEPLOY_DIR_IMAGE}/dtbs/dtb.img
}
do_merge_dtbs[cleandirs] = " \
     ${DEPLOY_DIR_IMAGE}/dtbs \
     ${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', '${DEPLOY_DIR_IMAGE}/dtbos ', ' ', d)} \
"

addtask do_merge_dtbs after do_image before do_makeboot

BOOT_RAMDISK_IMG ?= "${@bb.utils.contains('MACHINE_FEATURES', 'early-ramdisk-init', 'early-ramdisk-image-${PRODUCT}.cpio.lz4', '/dev/null', d)}"

python do_makeboot () {
    import subprocess

    mkboot_bin_path = d.getVar('STAGING_BINDIR_NATIVE', True) + "/scripts/mkbootimg.py"

    kernel_path = d.getVar('DEPLOY_DIR_IMAGE', True) + "/" + d.getVar('KERNEL_IMAGETYPE', True)
    dtb_path = d.getVar('DEPLOY_DIR_IMAGE', True) + "/dtbs/dtb.img"
    header_version = d.getVar('KERNEL_IMAGE_HEADER_VERSION', True)
    cmdline = "\"" + d.getVar('KERNEL_CMD_PARAMS', True) + "\""
    pagesize = d.getVar('PAGE_SIZE', True)
    base = d.getVar('KERNEL_BASE', True)
    ramdisk = d.getVar('BOOT_RAMDISK_IMG', True)
    output = d.getVar('BOOTIMAGE_TARGET', True)

    # cmd to make boot.img
    cmd = mkboot_bin_path + " --kernel %s --dtb %s --cmdline %s --pagesize %s --base %s --header_version %s --ramdisk %s --output %s" \
        % (kernel_path, dtb_path, cmdline, pagesize, base, header_version, ramdisk, output )

    bb.debug(1, "do_makeboot cmd: %s" % (cmd))
    try:
        ret = subprocess.check_output(cmd, shell=True)
    except RuntimeError as e:
         bb.error("do_makeboot cmd: %s failed with error %s" % (cmd, str(e)))

    if bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', 'true', 'false', d):
        cmd = "mkdtimg create %s-dtbo.img --page_size=%s dtbos/*.dtbo" \
            % (d.getVar('PRODUCT'), d.getVar('PAGE_SIZE'))
        bb.debug(1, "mkdtimg cmd: %s" % (cmd))
        subprocess.call(cmd, shell=True)
}

do_makeboot[dirs] = "${DEPLOY_DIR_IMAGE}"
# Make sure native tools and vmlinux ready to create boot.img
do_makeboot[depends] += "virtual/kernel:do_deploy mkbootimg-native:do_populate_sysroot"
do_makeboot[depends] += "${@bb.utils.contains('MACHINE_FEATURES', 'early-ramdisk-init', 'early-ramdisk-image:do_image_complete', ' ', d)}"
do_makeboot[sstate-inputdirs] = "${DEPLOY_DIR_IMAGE}"
do_makeboot[sstate-outputdirs] = "${DEPLOY_DIR_IMAGE}"
do_makeboot[stamp-extra-info] = "${MACHINE_ARCH}"

python do_makeboot_setscene () {
    sstate_setscene(d)
}

# create dummy vendor-boot & vbmeta image
# create dummy boot-init image for Andriod container
do_make_vendor_boot() {
    if ${@bb.utils.contains('MACHINE_FEATURES', 'vendor-boot-image', 'true', 'false', d)}; then
        dd if=/dev/zero of=${VENDORBOOTIMAGE_TARGET} bs=1M count=48;
        dd if=/dev/zero of=${BOOTINIT_TARGET} bs=1K count=1;
    fi
}
do_make_vendor_boot[dirs] = "${DEPLOY_DIR_IMAGE}"

addtask do_make_vendor_boot after do_makeboot before do_sign_boot_img

#sign boot, dtbo and vendor-boot img
do_sign_boot_img () {
    imgname="${DEPLOY_DIR_IMAGE}/${BOOTIMAGE_TARGET}"
    if ${@bb.utils.contains('DISTRO_FEATURES', 'qti-avb', 'true', 'false', d)}; then
        avb_sign_boot_image ${imgname}
    fi
}

avb_sign_boot_image() {
    img="$1"
    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'true', 'false', d)}; then
        # For lvgvm avb2.0, add hash for boot image.
        avbtool add_hash_footer  \
            --image ${img}  \
            --partition_size 0x04000000  \
            --partition_name boot \
            --algorithm SHA256_RSA4096 \
            --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/vbgvm_private_key_4096.pem \
            --rollback_index 0

    else
        # For lv avb2.0, add hash for boot image, dtbo image and vendor-boot image.
        avbtool add_hash_footer  \
            --image ${img}  \
            --partition_size 0x04000000  \
            --partition_name boot \
            --algorithm SHA256_RSA4096 \
            --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/testkey_rsa4096.pem \
            --rollback_index 0
        if [ -f ${DEPLOY_DIR_IMAGE}/${PRODUCT}-vendor_boot.img ]; then
            avbtool add_hash_footer  \
                --image ${DEPLOY_DIR_IMAGE}/${PRODUCT}-vendor_boot.img  \
                --partition_size 0x04000000  \
                --partition_name vendor_boot \
                --algorithm SHA256_RSA4096 \
                --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/testkey_rsa4096.pem \
                --rollback_index 0
        fi
        avbtool add_hash_footer  \
            --image ${DEPLOY_DIR_IMAGE}/${PRODUCT}-dtbo.img  \
            --partition_size 0x00200000 \
            --partition_name dtbo \
            --algorithm SHA256_RSA4096 \
            --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/testkey_rsa4096.pem \
            --rollback_index 0
    fi
}
#Sign boot image after generation
do_sign_boot_img[dirs] = "${DEPLOY_DIR_IMAGE}"

addtask do_makeboot_setscene

addtask do_makeboot after do_merge_dtbs before do_sign_boot_img
addtask do_sign_boot_img after do_makeboot before do_build
