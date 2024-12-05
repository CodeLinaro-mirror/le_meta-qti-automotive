SUMMARY = "Utilities to enhance systemd power interactions"
DESCRIPTION = "Provides systemd units as infra to hook services into systemd sleep logic"
HOMEPAGE = "https://git.codelinaro.org"
SECTION = "base"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "glibc glib-2.0 systemd"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/safelinux-services/power-utils/.git;protocol=${PROTO};destsuffix=/vendor/qcom/opensource/safelinux-services/power-utils;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/vendor/qcom/opensource/safelinux-services/power-utils"

inherit systemd cmake pkgconfig

SYSTEMD_SERVICE:${PN} = "\
    check-inhibitors.service \
    sleep-bounds.target \
    trigger-resume.service \
    failure-resume.service \
    sleep-apps.target \
    sleep-drivers.target \
    make-pm-dir.service \
"

FILES:${PN} += "\
    ${systemd_system_unitdir}/* \
    ${libdir}/tmpfiles-early.d/* \
"
