SUMMARY = "Package group to bring in packages for LVGVM"
DESCRIPTION = "Grouping of programs for running LVGVM on Embedded Linux System"

PROVIDES = "${PACKAGES}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-lvgvm \
"

ALLOW_EMPTY:${PN} = "1"

GVM_NET_CONFIG_LVGVM ?= "gvm-net-config-lvgvm"
GVM_NET_CONFIG_LVGVM:gen5 = "gvm-net-configure-lvgvm"

RDEPENDS:${PN} = "\
    qcrosvm-lvgvm \
    vhost-user-q-lvgvm \
    vhost-user-scmi-lvgvm \
    ${GVM_NET_CONFIG_LVGVM} \
    dspfirmware-mount-lvgvm \
"

RDEPENDS:${PN}:remove:sa7255 = "vhost-user-scmi-lvgvm"
