DESCRIPTION = "The minimal set of packages required to boot the system"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-core-boot \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    ${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '6.12', 'platformdlkm ', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'ab-boot-support', 'abctl ab-status-updater', '', d)} \
    packagegroup-core-boot \
    "
