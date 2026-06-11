SUMMARY = "Scripts for setup Host Network"
DESCRIPTION = "This is a scripts about automatic setup network, \
it can help us to setup LV Host Network"
HOMEPAGE = "https://git.codelinaro.org/"
SECTION = "network"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

SRC_URI = "\
    file://setup-network-host-gunyah.sh \
    file://setup-network-host-gunyah.service \
    file://setup-network-host-gunyah-vmm_sa7255.sh \
    file://setup-network-host-gunyah-vmm_sa8775.sh \
    file://setup-network-host-gunyah-vmm.service \
    file://setup-network-host-gunyah_gen5.sh \
    file://setup-network-host-gunyah_gen5.service \
    file://setup-network-host-gunyah-vmm_gen5.sh \
    file://setup-network-host-gunyah-vmm_gen5.service \
    file://gvm_net_run.sh \
"

inherit systemd

do_install:append:sa8775() {
  install -d ${D}${bindir}
  install -d ${D}${systemd_system_unitdir}

  if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-vmm', 'true', 'false', d)}; then
      install -m 0755 ${WORKDIR}/setup-network-host-gunyah-vmm_sa8775.sh ${D}${bindir}/setup-network-host.sh
      install -m 0644 ${WORKDIR}/setup-network-host-gunyah-vmm.service ${D}${systemd_unitdir}/system/setup-network-host.service
  else
      install -m 0755 ${WORKDIR}/setup-network-host-gunyah.sh ${D}${bindir}/setup-network-host.sh
      install -m 0644 ${WORKDIR}/setup-network-host-gunyah.service ${D}${systemd_unitdir}/system/setup-network-host.service
  fi
}

do_install:append:sa7255() {
  install -d ${D}${bindir}
  install -d ${D}${systemd_system_unitdir}

  if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-vmm', 'true', 'false', d)}; then
      install -m 0755 ${WORKDIR}/setup-network-host-gunyah-vmm_sa7255.sh ${D}${bindir}/setup-network-host.sh
      install -m 0644 ${WORKDIR}/setup-network-host-gunyah-vmm.service ${D}${systemd_unitdir}/system/setup-network-host.service
  else
      install -m 0755 ${WORKDIR}/setup-network-host-gunyah.sh ${D}${bindir}/setup-network-host.sh
      install -m 0644 ${WORKDIR}/setup-network-host-gunyah.service ${D}${systemd_unitdir}/system/setup-network-host.service
  fi
}

do_install:append:gen5() {

  install -d ${D}${bindir}
  install -d ${D}${systemd_system_unitdir}

  install -m 0755 ${WORKDIR}/setup-network-host-gunyah-vmm_gen5.sh ${D}${bindir}/setup-network-host-vmm.sh
  install -m 0644 ${WORKDIR}/setup-network-host-gunyah-vmm_gen5.service ${D}${systemd_unitdir}/system/setup-network-host-vmm.service
  install -m 0755 ${WORKDIR}/setup-network-host-gunyah_gen5.sh ${D}${bindir}/setup-network-host.sh
  install -m 0644 ${WORKDIR}/setup-network-host-gunyah_gen5.service ${D}${systemd_unitdir}/system/setup-network-host.service

  install -m 0755 ${WORKDIR}/gvm_net_run.sh ${D}${bindir}/gvm_net_run.sh
}

SYSTEMD_SERVICE:${PN} = "setup-network-host.service"
SYSTEMD_SERVICE:${PN}:append:gen5 = " setup-network-host-vmm.service"
