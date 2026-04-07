require machine-image-lagvm.bb

SUMMARY = "Machine image"
DESCRIPTION = "Build the full machine image depend on different parameters"
LICENSE = "BSD-3-Clause"

IMAGE_INSTALL += "\
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-vmm', 'packagegroup-qti-lvgvm', '', d)} \
"

DEPLOY_NAME_BASE = "${PRODUCT}-automotive"
USR_IMAGE_BASENAME = "${PRODUCT}-usrfs"
PERSIST_IMAGE_BASENAME = "${PRODUCT}-persist"
BOOTIMAGE_TARGET = "${PRODUCT}-boot.img"
