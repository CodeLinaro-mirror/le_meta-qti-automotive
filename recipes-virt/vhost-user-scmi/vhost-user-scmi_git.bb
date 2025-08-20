SUMMARY = "Vhost user scmi binary"
DESCRIPTION = "vhost user scmi implements the SCMI protocol, providing interface to configure the protocol informations. It will also process the SCMI message from Guest VM, and translate to IOCTLs in Host VM."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

SYSTEMD_SERVICE:${PN} = "vhost-user-scmi.service"
SYSTEMD_SERVICE:${PN}:append:sa8255-ivi = " vhost-user-scmi-lv.service"
SYSTEMD_SERVICE:${PN}:append:sa8775-flex = " vhost-user-scmi-lv.service"

DEPENDS += "safelinux-cfg-modules vhost-user-lib"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/vhost-user-scmi/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/vhost-user-scmi;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/vhost-user-scmi"

do_install:append:sa8797() {
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/vhost-user-scmi-sa8797.service ${D}/${systemd_unitdir}/system/vhost-user-scmi.service
}

do_install:append:sa8775() {
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/vhost-user-scmi.service ${D}/${systemd_unitdir}/system/vhost-user-scmi.service
}

do_install:append:sa8255-ivi() {
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/vhost-user-scmi-lv.service ${D}/${systemd_unitdir}/system/vhost-user-scmi-lv.service
}

do_install:append:sa8775-flex() {
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/vhost-user-scmi-lv.service ${D}/${systemd_unitdir}/system/vhost-user-scmi-lv.service
}

inherit cmake systemd
