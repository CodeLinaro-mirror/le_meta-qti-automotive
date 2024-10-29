SUMMARY = "Scripts for setup Host Network"
DESCRIPTION = "This is a scripts about automatic setup network, \
it can help us to setup LV Host Network"
HOMEPAGE = "https://git.codelinaro.org/"
SECTION = "network"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

SRC_URI = "\
    file://setup-network-host.sh \
    file://setup-network-host.service \
    file://setup-network-host-gunyah.sh \
    file://setup-network-host-gunyah.service \
    file://setup-network-host-gunyah-vmm.sh \
    file://setup-network-host-gunyah-vmm.service \
"

inherit systemd

do_install() {
  install -d ${D}${systemd_system_unitdir}
  install -d ${D}${bindir}

  if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-gunyah', 'true', 'false', d)}; then
      if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-vmm', 'true', 'false', d)}; then
          install -m 0755 ${WORKDIR}/setup-network-host-gunyah-vmm.sh ${D}${bindir}/setup-network-host.sh
          install -m 0644 ${WORKDIR}/setup-network-host-gunyah-vmm.service ${D}${systemd_unitdir}/system/setup-network-host.service
      else
          install -m 0755 ${WORKDIR}/setup-network-host-gunyah.sh ${D}${bindir}/setup-network-host.sh
          install -m 0644 ${WORKDIR}/setup-network-host-gunyah.service ${D}${systemd_unitdir}/system/setup-network-host.service
      fi
  else
      install -m 0755 ${WORKDIR}/setup-network-host.sh ${D}${bindir}/setup-network-host.sh
      install -m 0644 ${WORKDIR}/setup-network-host.service ${D}${systemd_unitdir}/system/setup-network-host.service
  fi
}

SYSTEMD_SERVICE:${PN} = "setup-network-host.service"
