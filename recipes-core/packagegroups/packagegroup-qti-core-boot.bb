DESCRIPTION = "The minimal set of packages required to boot the system"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-core-boot \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'platformdlkm ', '', d)} \
    ${@bb.utils.contains_any('PREFERRED_PROVIDER_virtual/kernel', 'linux-ack', 'soc-modules', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'ab-boot-support', 'abctl ab-status-updater', '', d)} \
    packagegroup-core-boot \
    "
