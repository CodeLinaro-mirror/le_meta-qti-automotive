SUMMARY = "Package group to support userspace drivers"
DESCRIPTION = "Grouping of programs for userspace drivers on Linux System"

PROVIDES = "${PACKAGES}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-umd \
"

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} = "\
    safelinux-cfg-modules \
    safelinux-system-cfg \
    safelinux-dbg-modules \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-gunyah', 'dspfirmware-mount', '', d)} \
"
