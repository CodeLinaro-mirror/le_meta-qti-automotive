DESCRIPTION = "The minimal set of packages required to boot the system"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-core-boot \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    packagegroup-core-boot \
    "
