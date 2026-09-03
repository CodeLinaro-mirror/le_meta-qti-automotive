SUMMARY = "Implement the early eth script"
DESCRIPTION = "Implement the early eth script to bring up the eth interface in early time during boot up"
HOMEPAGE = "https://git.codelinaro.org/"
SECTION = "network"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

SRC_URI = "\
    file://early_eth0.service \
    file://early_eth1.service \
    file://early_eth0_sa7255.sh \
    file://early_eth0_sa7255.service \
    file://eth_module_load.sh \
    file://eth_module_load.service \
"

inherit systemd useradd

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "early-eth"
USERADD_PARAM:${PN} = "--no-create-home -g early-eth --shell /bin/false early-eth"

do_install:append() {
    install -d ${D}${systemd_system_unitdir}
    install -d ${D}${bindir}
    install -m 0644 ${WORKDIR}/early_eth0.service ${D}${systemd_unitdir}/system/early_eth0.service
    install -m 0644 ${WORKDIR}/early_eth1.service ${D}${systemd_unitdir}/system/early_eth1.service
    install -m 0755 ${WORKDIR}/eth_module_load.sh ${D}${bindir}/eth_module_load.sh
    install -m 0644 ${WORKDIR}/eth_module_load.service ${D}${systemd_unitdir}/system/eth_module_load.service
}

do_install:append:sa8775() {
    sed -i 's/^WantedBy=.*$/WantedBy=sysinit.target/' ${D}${systemd_unitdir}/system/early_eth1.service
}

do_install:append:sa7255() {
    install -m 0755 ${WORKDIR}/early_eth0_sa7255.sh ${D}${bindir}/early_eth0_sa7255.sh
    install -m 0644 ${WORKDIR}/early_eth0_sa7255.service ${D}${systemd_unitdir}/system/early_eth0_sa7255.service
}

SYSTEMD_SERVICE:${PN} = "\
       early_eth0.service \
       early_eth1.service \
"

SYSTEMD_SERVICE:${PN}:sa7255 = "\
       early_eth0_sa7255.service \
"

SYSTEMD_AUTO_ENABLE:${PN} = "enable"

FILES:${PN} += "\
     ${systemd_unitdir}/system/early_eth0.service \
     ${systemd_unitdir}/system/early_eth1.service \
     ${systemd_unitdir}/system/early_eth0_sa7255.service \
     ${bindir}/early_eth0_sa7255.sh \
     ${systemd_unitdir}/system/eth_module_load.service \
     ${bindir}/eth_module_load.sh \
"
