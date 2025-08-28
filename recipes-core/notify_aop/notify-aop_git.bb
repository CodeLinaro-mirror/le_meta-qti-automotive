SUMMARY = "Notify AOP from APSS with suspend mode"
DESCRIPTION = "Provides infra to communicate sleep mode to AOP"
HOMEPAGE = "https://git.codelinaro.org"
SECTION = "base"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "virtual/kernel-headers libstd systemd libkiumd power-utils libpil-client"
DEPENDS += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'safelinux-cfg-modules', '', d)}"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/safelinux-services/notify_aop/.git;protocol=${PROTO};destsuffix=/vendor/qcom/opensource/safelinux-services/notify_aop;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/vendor/qcom/opensource/safelinux-services/notify_aop"

inherit systemd cmake pkgconfig sleep-notify-service

do_install:append() {
      install -m 0644 ${S}/sleep-notify@notify_aop.service.d/notify_aop.conf -D ${D}${systemd_system_unitdir}/sleep-notify@notify_aop.service.d/notify_aop.conf
}

SYSTEMD_SERVICE:${PN} = "sleep-notify@notify_aop.service"

FILES:${PN} += "\
    ${systemd_unitdir}/* \
    ${systemd_system_unitdir}/sleep-notify@notify_aop.service.d/* \
"


CXXFLAGS += "\
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', '-I${STAGING_INCDIR}/', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', '-I${STAGING_INCDIR}/uapi', '', d)} \
    -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel} \
"

CFLAGS += "\
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', '-I${STAGING_INCDIR}/', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', '-I${STAGING_INCDIR}/uapi', '', d)} \
    -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel} \
"
