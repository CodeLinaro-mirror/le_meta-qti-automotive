SUMMARY = "QTI ARK Kernel Boot image"
DESCRIPTION = "Build QTI ARK kernel boot image"
LICENSE = "BSD-3-Clause-Clear"

DEPENDS += "mkbootimg-native mkdtimg-native openssl-native  python3-native virtual/kernel"
DEPENDS += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'oot-dtbo', '', d)}"
DEPENDS += "${@bb.utils.contains_any('MACHINE', 'sa8797', bb.utils.contains('MACHINE_FEATURES', 'qti-multimedia', 'mm-vfio-devicetree', '', d), '', d)}"
DEPENDS += "${@bb.utils.contains('COMBINED_FEATURES', 'qti-audio-aw', 'audiolite-devicetree', '', d)}"
DEPENDS += "${@bb.utils.contains_any('COMBINED_FEATURES', 'qti-audio qti-audio-ar', bb.utils.contains('MACHINE_FEATURES', 'qti-gunyah qti-umd', 'audiolite-devicetree', '', d), '', d)}"

IMAGE_CLASSES:remove = "qimage"
IMAGE_FEATURES:remove = "ssh-server-openssh"

inherit image ark-dtb-merge

EXTRA_IMAGE_FEATURES = ""

KERNEL_VERSION = "${@oe.utils.read_file('${STAGING_KERNEL_BUILDDIR}/kernel-abiversion')}"

do_make_dtb() {
    install -d ${DEPLOY_DIR_IMAGE}/build-artifacts/techpack-dtbs
    install -d ${DEPLOY_DIR_IMAGE}/build-artifacts/dtb
    install -d ${DEPLOY_DIR_IMAGE}/dtbs

    dtb_dir=${DEPLOY_DIR_IMAGE}/build-artifacts/dtb
    dtbo_dir=${DEPLOY_DIR_IMAGE}/build-artifacts/techpack-dtbs
    out_dir=${DEPLOY_DIR_IMAGE}/dtbs

    merge_dtbos $dtb_dir $dtbo_dir $out_dir

    cat ${DEPLOY_DIR_IMAGE}/dtbs/*.dtb* > ${DEPLOY_DIR_IMAGE}/dtbs/dtb.img
}
do_make_dtb[depends] += "virtual/kernel:do_deploy"
do_make_dtb[depends] += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'oot-dtbo:do_deploy', '', d)}"
do_make_dtb[depends] += "${@bb.utils.contains_any('MACHINE', 'sa8797', bb.utils.contains('MACHINE_FEATURES', 'qti-multimedia', 'mm-vfio-devicetree:do_deploy', '', d), '', d)}"
do_make_dtb[depends] += "${@bb.utils.contains('COMBINED_FEATURES', 'qti-audio-aw', 'audiolite-devicetree:do_deploy', '', d)}"
do_make_dtb[depends] += "${@bb.utils.contains_any('COMBINED_FEATURES', 'qti-audio qti-audio-ar', bb.utils.contains('MACHINE_FEATURES', 'qti-gunyah qti-umd', 'audiolite-devicetree:do_deploy', '', d), '', d)}"

addtask do_make_dtb after do_image before do_makeboot

BOOT_RAMDISK_IMG ?= "${@bb.utils.contains('MACHINE_FEATURES', 'early-ramdisk-init', 'early-ramdisk-image-${PRODUCT}.cpio.lz4', '/dev/null', d)}"

do_makeboot () {
    if [ "${KERNEL_IMAGE_HEADER_VERSION}" = "2" ]; then
        # Make bootimage
        ${STAGING_BINDIR_NATIVE}/scripts/mkbootimg.py --header_version ${KERNEL_IMAGE_HEADER_VERSION} \
        --kernel  ${DEPLOY_DIR_IMAGE}/Image \
        --dtb  ${DEPLOY_DIR_IMAGE}/dtbs/dtb.img \
        --ramdisk ${BOOT_RAMDISK_IMG} \
        --pagesize ${PAGE_SIZE} \
        --base ${KERNEL_BASE} \
        --ramdisk_offset 0x0 \
        --cmdline "${KERNEL_CMD_PARAMS}" \
        --output  ${DEPLOY_DIR_IMAGE}/${PRODUCT}-boot-${KERNEL_VERSION}.img
    elif [ "${KERNEL_IMAGE_HEADER_VERSION}" = "1" ]; then
        # Make bootimage
        ${STAGING_BINDIR_NATIVE}/scripts/mkbootimg.py \
        --kernel  ${DEPLOY_DIR_IMAGE}/Image.gz-dtb \
        --ramdisk ${BOOT_RAMDISK_IMG} \
        --pagesize ${PAGE_SIZE} \
        --base ${KERNEL_BASE} \
        --ramdisk_offset 0x0 \
        --cmdline "${KERNEL_CMD_PARAMS}" \
        --output  ${DEPLOY_DIR_IMAGE}/${PRODUCT}-boot-${KERNEL_VERSION}.img
    else
        echo "Unknown Boot Image Header Version"
        return 1
    fi
    cp ${DEPLOY_DIR_IMAGE}/${PRODUCT}-boot-${KERNEL_VERSION}.img ${DEPLOY_DIR_IMAGE}/${PRODUCT}-boot.img
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

#sign boot img
do_sign_boot_img () {
    imgname="${DEPLOY_DIR_IMAGE}/${BOOTIMAGE_TARGET}"
    if ${@bb.utils.contains('DISTRO_FEATURES', 'qti-avb', 'true', 'false', d)}; then
        avb_sign_boot_image ${imgname}
    fi
}

avb_sign_boot_image() {
    img="$1"
    avbtool add_hash_footer  \
        --image ${img}  \
        --partition_size 0x04000000  \
        --partition_name boot \
        --algorithm SHA256_RSA4096 \
        --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/testkey_rsa4096.pem \
        --rollback_index 0
    if ${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', 'true', 'false', d)}; then
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
do_sign_boot_img[depends] += "sectools-native:do_populate_sysroot avbtool-native:do_populate_sysroot"

addtask do_makeboot_setscene

addtask do_makeboot before do_build
addtask do_sign_boot_img after do_makeboot before do_build

do_rootfs[noexec] = "1"
do_image[noexec] = "1"
do_image_complete[noexec] = "1"
