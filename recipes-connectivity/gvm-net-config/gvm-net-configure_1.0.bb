SUMMARY = "Service to set up the network bridge for the Android GVM"
DESCRIPTION = "Service to set up the network bridge for the Android GVM"
HOMEPAGE = "https://git.codelinaro.org/"
SECTION = "network"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

SRC_URI = "\
    file://gvm-net-config.sh \
    file://gvm-net-config.service \
"

inherit systemd

do_install() {
  install -d ${D}${bindir}


  install -d ${D}${systemd_system_unitdir}

  install -m 0755 ${WORKDIR}/gvm-net-config.sh ${D}${bindir}/gvm-net-config.sh
  install -m 0644 ${WORKDIR}/gvm-net-config.service ${D}${systemd_unitdir}/system/gvm-net-config.service

}

SYSTEMD_SERVICE:${PN} = "gvm-net-config.service"
