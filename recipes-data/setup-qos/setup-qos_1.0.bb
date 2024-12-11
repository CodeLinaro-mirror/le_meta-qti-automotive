SUMMARY = "Scripts for setup QoS configuration"
DESCRIPTION = "This is a scripts about automatic setup QoS, \
configuration during runtime"
HOMEPAGE = "https://git.codelinaro.org/"
SECTION = "network"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

SRC_URI = "\
    file://setup_eth0.service \
    file://setup_eth1.service \
    file://config.ini \
    file://setup_eth.sh \
"

inherit systemd

do_install() {
  if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
    install -d ${D}${sysconfdir}/initscripts
    install -m 0755 ${WORKDIR}/setup_eth.sh ${D}${sysconfdir}/initscripts
    install -m 0755 ${WORKDIR}/config.ini ${D}${sysconfdir}/initscripts

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/setup_eth0.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${WORKDIR}/setup_eth1.service ${D}${systemd_unitdir}/system/
  fi
}

SYSTEMD_SERVICE:${PN} = "\
       setup_eth0.service \
       setup_eth1.service \
"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

FILES:${PN} += "\
    ${systemd_unitdir}/system/setup_eth0.service \
    ${systemd_unitdir}/system/setup_eth1.service \
    ${sysconfdir}/initscripts/setup_eth.sh \
    ${sysconfdir}/initscripts/config.ini \
"

