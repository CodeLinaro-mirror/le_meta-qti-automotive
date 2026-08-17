SUMMARY = "Package group to bring in packages for running VMs"
DESCRIPTION = "Grouping of programs for running VMs on Embedded Linux System"

PROVIDES = "${PACKAGES}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-vmm \
"

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} = "\
    qcrosvm \
    gunyah-drivers \
    msmhab \
    android-tools \
    vhost-user-q \
    vhost-user-lib \
    vhost-user-scmi \
    gvm-net-config \
    vhost-device-i2c \
    vhost-device-ssr \
    hyp-udmabuf \
    hyp-udmabuf-test \
    vmm-boot-lcm \
    vm-powerctl \
    dspfirmware-mount-vmm \
"

RDEPENDS:${PN}:append:gen5 = " \
    virtiofsd \
    vhost-device-gpio \
"
RDEPENDS:${PN}:remove:gen5 = "\
    gvm-net-config \
"
