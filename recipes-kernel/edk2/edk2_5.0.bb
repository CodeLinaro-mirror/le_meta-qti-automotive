SUMMARY = "edk2"
DESCRIPTION = "UEFI bootloader"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-2-Clause & BSD-3-Clause"
LIC_FILES_CHKSUM = "\
    file://${COMMON_LICENSE_DIR}/BSD-2-Clause;md5=cb641bc04cda31daea161b1bc15da69f \
    file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
"

require recipes-kernel/edk2/edk2-common.inc

EARLY_ETH = "${@bb.utils.contains('DISTRO_FEATURES', 'qti-early-eth', '1', '0', d)}"
HIBERNATION = "${@bb.utils.contains('COMBINED_FEATURES', 'hibernation', '1', '0', d)}"
AB_BOOT_LXC = "${@bb.utils.contains('MACHINE_FEATURES', 'qti-lxc', '1', '0', d)}"

SRC_URI = "${PATH_TO_REPO}/kernel/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform/bootable/bootloader/edk2/.git;protocol=${PROTO};destsuffix=kernel/kernel-${PREFERRED_VERSION_linux-msm}/kernl_platform/bootable/bootloader/edk2;usehead=1"

SRC_URI:append = " file://0001-edk2-remove-register-for-C-17-compiler.patch"

S = "${WORKDIR}/kernel/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform/bootable/bootloader/edk2"
