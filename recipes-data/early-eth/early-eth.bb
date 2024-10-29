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
"

inherit systemd

do_install:append() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/early_eth0.service ${D}${systemd_unitdir}/system/early_eth0.service
    install -m 0644 ${WORKDIR}/early_eth1.service ${D}${systemd_unitdir}/system/early_eth1.service
}

SYSTEMD_SERVICE:${PN} = "\
       early_eth0.service \
       early_eth1.service \
"

SYSTEMD_AUTO_ENABLE:${PN} = "enable"

FILES:${PN} += "\
     ${systemd_unitdir}/system/early_eth0.service \
     ${systemd_unitdir}/system/early_eth1.service \
"
