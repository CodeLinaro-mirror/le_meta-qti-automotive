SUMMARY = "Load kernel modules late"
DESCRIPTION = "\
This service is used to load kernel modules that early services not need."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

SYSTEMD_SERVICE:${PN} = "\
    modules-load-late.service \
"
SRC_URI = "\
    file://modules-load-late.service"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}"

inherit systemd

do_install:append() {
    install -d ${D}${systemd_unitdir}
    install -m 0644 ${S}/modules-load-late.service -D ${D}${systemd_unitdir}/system/modules-load-late.service
}

FILES:${PN} += "${systemd_unitdir}/system/modules-load-late.service"

