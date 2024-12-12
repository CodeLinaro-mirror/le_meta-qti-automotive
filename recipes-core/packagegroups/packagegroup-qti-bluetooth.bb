SUMMARY = "QTI package group for bluetooth"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-bluetooth \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    bt-dlkm \
    "
