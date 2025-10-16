SUMMARY = "QTI package group for data service"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-data \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    bridge-utils \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'setup-network', '', d)} \
    setup-network-host \
    net-tools \
    ethtool \
    iperf2 \
    iperf3 \
    iproute2 \
    iproute2-ss \
    iproute2-tc \
    tcpdump \
    phytool \
    vlan \
    strongswan \
    tcp-wrappers \
    ${@bb.utils.contains('LAYERSERIES_CORENAMES', 'scarthgap', '', 'netkit-telnet', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '', 'proftpd', d)} \
    ${@bb.utils.contains_any('MACHINE_FEATURES', 'qti-umd', 'setup-qos', '', d)} \
    ${@bb.utils.contains_any('MACHINE_FEATURES', 'qti-umd', 'early-eth', '', d)} \
"

RDEPENDS:${PN}:append:gen5 = " \
    gvm-net-configure \
    ${@bb.utils.contains_any('MACHINE_FEATURES', 'qti-umd', 'setup-network-param', '', d)} \
"

RDEPENDS:${PN}:append:sa8775 = " ${@bb.utils.contains_any('MACHINE_FEATURES', 'qti-umd', 'netlink-service-infra', '', d)}"

RDEPENDS:${PN}:remove:sa8650-adas = "netlink-service-infra"

RDEPENDS:${PN}:append:quin-gvm-lemans = " dataeth-dlkm"
