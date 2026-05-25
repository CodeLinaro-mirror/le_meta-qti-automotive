require machine-image-pvm.bb

SUMMARY = "Machine image - single LAGVM"
DESCRIPTION = "Build the machine image with single LAGVM"
LICENSE = "BSD-3-Clause-Clear"

IMAGE_INSTALL += "\
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-vmm', 'packagegroup-qti-vmm', '', d)} \
"

DEPLOY_NAME_BASE = "${PRODUCT}-lagvm-automotive"
USR_IMAGE_BASENAME = "${PRODUCT}-lagvm-usrfs"
PERSIST_IMAGE_BASENAME = "${PRODUCT}-lagvm-persist"
BOOTIMAGE_TARGET = "${PRODUCT}-lagvm-boot.img"
