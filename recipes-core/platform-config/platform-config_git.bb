SUMMARY = "Enable Qualcomm systemd services to use specific slices for cgroup management."
DESCRIPTION = "systemd slices are a systemd abstraction for cgroups. \
               This package enables Qualcomm systemd services to use different slices, \
               allowing them to choose the appropriate cgroup for each service."

HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "systemd"
DEPENDS:append:gen5 = " libsoftsku minini"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/safelinux-system-cfg/platform-config/.git;protocol=${PROTO};destsuffix=/vendor/qcom/opensource/safelinux-system-cfg/platform-config;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/vendor/qcom/opensource/safelinux-system-cfg/platform-config"

EXTRA_OECMAKE:append:sa8775-flex = " -D PVM_CPUS:STRING=0-3 -D GVM_CPUS:STRING=4-7"
EXTRA_OECMAKE:append:sa8255-ivi = " -D PVM_CPUS:STRING=0-1 -D GVM_CPUS:STRING=2-7"
#in gen5 slice dropin files are created by systemd-generators

inherit cmake pkgconfig systemd

SYSTEMD_SERVICE:${PN} = "\
    gvm.slice \
    pvm.slice \
    "

SYSTEMD_SERVICE:${PN}:append:gen5 = " offline-target-cpus.service reconfig-cgrp-slices.service"

do_install:append:gen5() {
    install -d ${D}${systemd_system_unitdir}/
    install -m 0444 ${S}/plat-config-generator/target_conf_safe_ivi.ini -D ${D}/etc/target_conf_safe_ivi.ini
    install -m 0444 ${S}/plat-config-generator/target_conf_nonsafe_ivi.ini -D ${D}/etc/target_conf_nonsafe_ivi.ini
    install -m 0444 ${S}/plat-config-generator/target_conf_flex.ini -D ${D}/etc/target_conf_flex.ini
    install -m 0444 ${S}/plat-config-generator/target_conf_adas.ini -D ${D}/etc/target_conf_adas.ini
    install -m 0644 ${S}/plat-config-generator/offline-target-cpus.service -D ${D}${systemd_system_unitdir}/offline-target-cpus.service
    install -m 0644 ${S}/plat-config-generator/reconfig-cgrp-slices.service -D ${D}${systemd_system_unitdir}/reconfig-cgrp-slices.service
}

RDEPENDS:${PN} += "minini"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""
FILES:${PN} += "${libdir}/*"
