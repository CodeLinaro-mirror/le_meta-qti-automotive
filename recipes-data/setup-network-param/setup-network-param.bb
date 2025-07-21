SUMMARY = "Script for Network parameters settings"
DESCRIPTION = "Custom sysctl configuration for network tuning"
HOMEPAGE = "https://www.kernel.org/doc/html/latest/admin-guide/sysctl/index.html"
SECTION = "network"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

SRC_URI = "\
    file://101-sysctl.conf \
"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "setup-network-param"
USERADD_PARAM:${PN} = "--no-create-home -g setup-network-param --shell /bin/false setup-network-param"

do_install() {
  if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
    install -d ${D}${sysconfdir}/sysctl.d
    install -m 0644 ${WORKDIR}/101-sysctl.conf ${D}${sysconfdir}/sysctl.d/101-sysctl.conf
  fi
}

FILES:${PN} += "\
    ${sysconfdir}/sysctl.d/101-sysctl.conf \
"
