SUMMARY = "QTI package group for bluetooth"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-bluetooth \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    bt-dlkm \
    bt-module-load \
    packagegroup-base-bluetooth \
    packagegroup-tools-bluetooth \
    "
