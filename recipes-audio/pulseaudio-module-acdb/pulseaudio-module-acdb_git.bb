SUMMARY = "PulseAudio Module ACDB"
DESCRIPTION = "This is the PulseAudio module used for audio calibration."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS += "acdbloader audcal glib-2.0 json-c pulseaudio"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/mm-audio/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/mm-audio;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/vendor/qcom/opensource/mm-audio/pulseaudio-module-acdb"

inherit autotools-brokensep pkgconfig

EXTRA_OECONF += "--enable-target=${AUDIO_BUILD_TARGET}"
EXTRA_OECONF += "--enable-acdbservice=yes"
EXTRA_OECONF += "--with-glib"
VERSION = "${@bb.utils.contains('LAYERSERIES_COMPAT_yocto', 'kirkstone', '15.0', '14.2', d)}"

do_configure:prepend () {
    sed -i -e "s|%PULSEAUDIO_VERSION%|${VERSION}|" ${S}/configure.ac
}

do_install:append() {
    install -d ${D}${sysconfdir}/pulse
    install -m 0755 ${S}/*.cfg -D ${D}${sysconfdir}/pulse
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILES:${PN} += "${libdir}/pulse-*/modules/"
FILES:${PN}-staticdev += "${libdir}/pulse-*/modules/*.a"
FILES:${PN}-dbg += "${libdir}/pulse-*/modules/.debug"

RDEPENDS:${PN} = "acdbloader"

AUDIO_BUILD_TARGET ?= "sa8155"
