SUMMARY = "Utilities to support unify build target"
DESCRIPTION = "Support different variant in single build by target"
HOMEPAGE = "https://git.codelinaro.org"
SECTION = "base"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "systemd"

SRC_URI = "file://targets-generator.sh"
SRC_URI:append = " file://services-enabler.sh"
SRC_URI:append = " file://safe-ivi.target"
SRC_URI:append = " file://nonsafe-ivi.target"
SRC_URI:append = " file://adas.target"
SRC_URI:append = " file://flex.target"
SRC_URI:append = " file://slt.target"
SRC_URI:append = " file://single-gvm.target"
SRC_URI:append = " file://multi-gvm.target"

S = "${WORKDIR}"

inherit systemd

SYSTEMD_SERVICE:${PN} = "\
    safe-ivi.target \
    nonsafe-ivi.target \
    adas.target \
    flex.target \
    slt.target \
    single-gvm.target \
    multi-gvm.target \
"
SYSTEMD_AUTO_ENABLE:${PN} = "disable"

FILES:${PN} += "${systemd_unitdir}/system-generators/targets-generator"
FILES:${PN} += "${systemd_unitdir}/system-generators/services-enabler"

do_install:append() {
    install -d ${D}/${systemd_unitdir}/system
    install -m 0644 ${S}/safe-ivi.target ${D}/${systemd_unitdir}/system/safe-ivi.target
    install -m 0644 ${S}/nonsafe-ivi.target ${D}/${systemd_unitdir}/system/nonsafe-ivi.target
    install -m 0644 ${S}/adas.target ${D}/${systemd_unitdir}/system/adas.target
    install -m 0644 ${S}/flex.target ${D}/${systemd_unitdir}/system/flex.target
    install -m 0644 ${S}/slt.target ${D}/${systemd_unitdir}/system/slt.target
    install -m 0644 ${S}/single-gvm.target ${D}/${systemd_unitdir}/system/single-gvm.target
    install -m 0644 ${S}/multi-gvm.target ${D}/${systemd_unitdir}/system/multi-gvm.target
    install -d ${D}/${systemd_unitdir}/system-generators
    install -m 0755 ${S}/targets-generator.sh ${D}/${systemd_unitdir}/system-generators/targets-generator
    install -m 0755 ${S}/services-enabler.sh ${D}/${systemd_unitdir}/system-generators/services-enabler
}
