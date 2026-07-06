#Copyright (c) 2022-2023, 2025 Qualcomm Innovation Center, Inc. All rights reserved.
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

DTB_OFFSET = "0x9300000"
DTB_OFFSET:gvm-gen5 = "0x9700000"
DTB_OFFSET:qclinux-gvm-gen5 = "0x9700000"

DTB_FILE_LIST:gvm-gen5 = "\
    ${DEPLOY_DIR_IMAGE}/dtbs/sa8797p-gunyah-vm-lv-qam.dtb \
    ${DEPLOY_DIR_IMAGE}/dtbs/sa8797p-v2-gunyah-vm-lv-qam.dtb \
"
DTB_FILE_LIST:gvm-gen4-5 = "\
    ${DEPLOY_DIR_IMAGE}/dtbs/monaco-gh-vm-lv-qam-ridesx.dtb \
    ${DEPLOY_DIR_IMAGE}/dtbs/lemans-gh-vm-lv-qam-ridesx.dtb \
    ${DEPLOY_DIR_IMAGE}/dtbs/lemans-gh-vm-lv-flex-qam-ridesx.dtb \
"

DTB_FILE_LIST:qclinux-gvm-gen5 = "\
    ${DEPLOY_DIR_IMAGE}/dtbs/sa8797p-gunyah-vm-lv-qam.dtb \
    ${DEPLOY_DIR_IMAGE}/dtbs/sa8797p-v2-gunyah-vm-lv-qam.dtb \
"

gvm_pilsplitter() {
    PILTOOLS_PATH="${STAGING_BINDIR_NATIVE}/scripts/pil_tools"
    DTB_CHECK=$(find ${DEPLOY_DIR_IMAGE}/build-artifacts/dtb -name "*.dtb" | sort)
    if [ -z "${DTB_CHECK}" ]; then
        echo "No *.dtb files found in $DEPLOY_DIR_IMAGE/dtbs"
        exit 1
    else
        # Use vb-dtb.img to avoid overwriting dtbs/dtb.img used by do_makeboot.
        # dtbs/dtb.img is the full cat *.dtb result for boot.img;
        # vb-dtb.img is the mkdtimg format for PIL signing (bootloader.img).
        # DTB_FILE_LIST defines per-machine DTBs with full paths.
        ${STAGING_BINDIR_NATIVE}/build/prebuilts/kernel-build-tools/linux-x86/bin/mkdtimg create \
            ${DEPLOY_DIR_IMAGE}/dtbs/vb-dtb.img \
            ${DTB_FILE_LIST}
    fi

    install -d ${DEPLOY_DIR_IMAGE}/signing

    # copy necessary files into signing dir
    cp ${DEPLOY_DIR_IMAGE}/Image ${DEPLOY_DIR_IMAGE}/signing
    cp ${DEPLOY_DIR_IMAGE}/LinuxLoader.efi ${DEPLOY_DIR_IMAGE}/signing
    cp ${DEPLOY_DIR_IMAGE}/FVMAIN_COMPACT.Fv ${DEPLOY_DIR_IMAGE}/signing
    cp ${DEPLOY_DIR_IMAGE}/ramdisk.img ${DEPLOY_DIR_IMAGE}/signing
    cp ${DEPLOY_DIR_IMAGE}/dtbs/vb-dtb.img ${DEPLOY_DIR_IMAGE}/signing/vb-dtb.img

    cd ${DEPLOY_DIR_IMAGE}/signing

    # autoghgvmlv-boot.elf is not a standard elf file, verify_elf in image_header.py
    # will fail and return 1.bypass yocto by adding 'set +e' and 'set -e'
    set +e
    python3 ${PILTOOLS_PATH}/image_header.py autoghgvmlv-boot.elf Image,0x0 vb-dtb.img,0x3000000 ramdisk.img,0x3100000 --32
    python3 ${PILTOOLS_PATH}/image_header.py autoghgvmlv-bootloader.elf FVMAIN_COMPACT.Fv,0x0 vb-dtb.img,${DTB_OFFSET} LinuxLoader.efi,0x9800000 --32
    set -e

    sectools secure-image autoghgvmlv-boot.elf --image-id GVM2 --security-profile ${STAGING_BINDIR_NATIVE}/${SECTOOLS_SECURITY_PROFILE} --sign --signing-mode TEST --outfile autoghgvmlv_signed-boot.elf
    sectools secure-image autoghgvmlv-boot.elf --inspect

    install -d ${DEPLOY_DIR_IMAGE}/signing/boot
    python3 ${PILTOOLS_PATH}/pil-splitter.py autoghgvmlv_signed-boot.elf boot/autoghgvmlv

    ${STAGING_BINDIR_NATIVE}/build/prebuilts/kernel-build-tools/linux-x86/bin/mkuserimg_mke2fs ${DEPLOY_DIR_IMAGE}/signing/boot ${DEPLOY_DIR_IMAGE}/vm-bootloader.img ext4 / 60000000

    sectools secure-image autoghgvmlv-bootloader.elf --image-id GVM2 --security-profile ${STAGING_BINDIR_NATIVE}/${SECTOOLS_SECURITY_PROFILE} --sign --signing-mode TEST --outfile autoghgvmlv_signed-bootloader.elf
    sectools secure-image autoghgvmlv-bootloader.elf --inspect

    install -d ${DEPLOY_DIR_IMAGE}/signing/bootloader
    python3 ${PILTOOLS_PATH}/pil-splitter.py autoghgvmlv_signed-bootloader.elf bootloader/autoghgvmlv

    ${STAGING_BINDIR_NATIVE}/build/prebuilts/kernel-build-tools/linux-x86/bin/mkuserimg_mke2fs ${DEPLOY_DIR_IMAGE}/signing/bootloader ${DEPLOY_DIR_IMAGE}/${PRODUCT}-bootloader.img ext4 / 6000000
}

do_gvm_pilsplitter() {
    touch ${DEPLOY_DIR_IMAGE}/ramdisk.img
    gvm_pilsplitter
}

do_gvm_pilsplitter[cleandirs] = "${DEPLOY_DIR_IMAGE}/signing"
do_gvm_pilsplitter[depends] += "virtual/guest-bootloader:do_deploy"
do_gvm_pilsplitter[depends] += "virtual/bootloader:do_deploy"

python () {
    if bb.utils.contains('MACHINE_FEATURES', 'qti-gvm', True, False, d):
        bb.build.addtask('do_gvm_pilsplitter', 'do_sign_boot_img', 'do_makeboot', d)
}

do_merge_dtbs[depends] += "virtual/kernel:do_deploy"
do_merge_dtbs[depends] += "virtual/kernel:do_shared_workdir"

do_merge_dtbs() {
    export PATH="${STAGING_KERNEL_BUILDDIR}/bin:${PATH}"
    install -d ${DEPLOY_DIR_IMAGE}/build-artifacts/techpack-dtbs
    install -d ${DEPLOY_DIR_IMAGE}/dtbs

    KERNEL_TARGET=autogvm ${STAGING_BINDIR_NATIVE}/build/android/merge_dtbs.py \
    --base ${DEPLOY_DIR_IMAGE}/build-artifacts/dtb \
    --techpack ${DEPLOY_DIR_IMAGE}/build-artifacts/techpack-dtbs \
    --out ${DEPLOY_DIR_IMAGE}/dtbs

    if ${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', 'true', 'false', d)}; then
        install -d ${DEPLOY_DIR_IMAGE}/build-artifacts/techpack-dtbos
        install -d ${DEPLOY_DIR_IMAGE}/dtbos
        KERNEL_TARGET=autogvm  ${STAGING_BINDIR_NATIVE}/build/android/merge_dtbs.py \
        --base ${DEPLOY_DIR_IMAGE}/build-artifacts/dtbo \
        --techpack ${DEPLOY_DIR_IMAGE}/build-artifacts/techpack-dtbos \
        --out ${DEPLOY_DIR_IMAGE}/dtbos
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
    boot_partition_size=$(avbtool calc_min_partition_size \
                          --image ${img} \
                          --partition_name boot \
                          --hash_algorithm sha256 \
                          --no_hashtree)
    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'true', 'false', d)}; then
        # For lvgvm avb2.0, add hash for boot image.
        avbtool add_hash_footer  \
            --image ${img}  \
            --partition_size ${boot_partition_size}  \
            --partition_name boot \
            --algorithm SHA256_RSA4096 \
            ${@bb.utils.contains('MACHINE_FEATURES', 'qti-gvm', '--key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/testkey_rsa4096.pem', '--key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/vbgvm_private_key_4096.pem', d)} \
            --rollback_index 0
            if [ -s ${DEPLOY_DIR_IMAGE}/${PRODUCT}-dtbo.img ]; then
               dtbo_partition_size=$(avbtool calc_min_partition_size \
                                   --image ${DEPLOY_DIR_IMAGE}/${PRODUCT}-dtbo.img \
                                   --partition_name dtbo \
                                   --hash_algorithm sha256 \
                                   --no_hashtree)
               avbtool add_hash_footer  \
                   --image ${DEPLOY_DIR_IMAGE}/${PRODUCT}-dtbo.img  \
                   --partition_size ${dtbo_partition_size} \
                   --partition_name dtbo \
                   --algorithm SHA256_RSA4096 \
                   --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/testkey_rsa4096.pem \
                   --rollback_index 0
           fi
    else
        # For lv avb2.0, add hash for boot image, dtbo image and vendor-boot image.
        avbtool add_hash_footer  \
            --image ${img}  \
            --partition_size ${boot_partition_size}  \
            --partition_name boot \
            --algorithm SHA256_RSA4096 \
            --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/testkey_rsa4096.pem \
            --rollback_index 0
        if [ -f ${DEPLOY_DIR_IMAGE}/${PRODUCT}-vendor_boot.img ]; then
            vendor_boot_partition_size=$(avbtool calc_min_partition_size \
                                         --image ${DEPLOY_DIR_IMAGE}/${PRODUCT}-vendor_boot.img \
                                         --partition_name vendor_boot \
                                         --hash_algorithm sha256 \
                                         --no_hashtree)
            avbtool add_hash_footer  \
                --image ${DEPLOY_DIR_IMAGE}/${PRODUCT}-vendor_boot.img  \
                --partition_size ${vendor_boot_partition_size}  \
                --partition_name vendor_boot \
                --algorithm SHA256_RSA4096 \
                --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/testkey_rsa4096.pem \
                --rollback_index 0
        fi
        dtbo_partition_size=$(avbtool calc_min_partition_size \
                              --image ${DEPLOY_DIR_IMAGE}/${PRODUCT}-dtbo.img \
                              --partition_name dtbo \
                              --hash_algorithm sha256 \
                              --no_hashtree)
        avbtool add_hash_footer  \
            --image ${DEPLOY_DIR_IMAGE}/${PRODUCT}-dtbo.img  \
            --partition_size ${dtbo_partition_size} \
            --partition_name dtbo \
            --algorithm SHA256_RSA4096 \
            --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/testkey_rsa4096.pem \
            --rollback_index 0
    fi
}
#Sign boot image after generation
do_sign_boot_img[dirs] = "${DEPLOY_DIR_IMAGE}"

addtask do_makeboot_setscene

addtask do_makeboot after do_merge_dtbs do_image before do_sign_boot_img
addtask do_sign_boot_img after do_makeboot before do_build
