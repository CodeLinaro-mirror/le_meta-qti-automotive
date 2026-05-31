SUMMARY = "Package group to bring in packages for LVGVM"
DESCRIPTION = "Grouping of programs for running LVGVM on Embedded Linux System"

PROVIDES = "${PACKAGES}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-lvgvm \
"

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} = "\
    qcrosvm-lvgvm \
    vhost-user-q-lvgvm \
    vhost-user-scmi-lvgvm \
    gvm-net-config-lvgvm \
    dspfirmware-mount-lvgvm \
"

RDEPENDS:${PN}:remove:sa7255 = "vhost-user-scmi-lvgvm"
RDEPENDS:${PN}:remove:gen5 = "\
    qcrosvm-lvgvm \
    vhost-user-scmi-lvgvm \
    gvm-net-config-lvgvm \
    dspfirmware-mount-lvgvm \
"
