SUMMARY = "Initialize PVM userdata directories"
DESCRIPTION = "Create the OverlayFS upper and work directories after mounting PVM userdata"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear"

SRC_URI = "file://pvm-userdata-init.service"

inherit systemd

SYSTEMD_SERVICE:${PN} = "pvm-userdata-init.service"
SYSTEMD_AUTO_ENABLE:${PN} = "disable"

do_install() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/pvm-userdata-init.service \
        ${D}${systemd_system_unitdir}/pvm-userdata-init.service
}

FILES:${PN} += "${systemd_system_unitdir}/pvm-userdata-init.service"
