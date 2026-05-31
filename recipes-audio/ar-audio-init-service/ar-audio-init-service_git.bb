
SUMMARY = "AR Audio Initialization Service"
DESCRIPTION = "This is an application that execute the post ar-audio-service.service tasks."
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "bootkpi-logging tinyalsa-new glib-2.0 agm"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/audio-ar-service/ar_init_service/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/audio-ar-service/ar_init_service;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/audio-ar-service/ar_init_service"

inherit cmake pkgconfig systemd

do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -m 0644 ${S}/ar-init-service.service -D ${D}${systemd_system_unitdir}/ar-init-service.service
    fi
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "ar-init-service.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

EXTRA_OECMAKE:append:gen5 = " -DENABLE_TARGET=sa8797"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

RDEPENDS:${PN} += "tinyalsa-new"
