SUMMARY = "AR Audio Service"
DESCRIPTION = "This is a daemon, which hosts multiple libraries to provide the capability for the clients to execute the usecase"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "audio-log-util agm ar-dev-plugin ar-audio-service-utils audio-device-manager"
DEPENDS += "glib-2.0 libkiumd libglink-client libglink-core fastrpc libpil-client systemd bootkpi-logging"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/audio-ar-service/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/audio-ar-service/audio_service;subpath=audio_service;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/audio-ar-service/audio_service"

inherit cmake pkgconfig systemd

CFLAGS += "-I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}"

do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -m 0644 ${S}/ar-audio-service.service -D ${D}${systemd_system_unitdir}/ar-audio-service.service
        install -m 0644 ${S}/audio-service-dbus.conf -D ${D}${sysconfdir}/dbus-1/system.d/audio-service-dbus.conf
    fi
}
SYSTEMD_SERVICE:${PN} = "\
        ar-audio-service.service \
    "

EXTRA_OECMAKE:append:gen5 = " -DENABLE_TARGET=sa8797"

RDEPENDS:${PN} += "libkiumd ssr-rm pil-rm glink-service-lrm fastrpc-rm diagservice"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""