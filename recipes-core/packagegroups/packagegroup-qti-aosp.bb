SUMMARY = "QTI package group for AOSP packages"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-aosp \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    system-core-adbd \
    system-core-leprop \
    system-core-post-boot \
    system-core-usb \
    ${@bb.utils.contains("MACHINE_FEATURES", "qti-hypervisor", "", "system-core-early-boot", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "qti-dlkm", "system-core-dlkm", "", d)} \
    "
