SUMMARY = "Wayland IVI Extension"
DESCRIPTION = "COVESA Layer Management API based on Wayland IVI Extension"
HOMEPAGE = "https://github.com/COVESA/wayland-ivi-extension"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1f1a56bb2dadf5f2be8eb342acf4ed79"

DEPENDS = "weston wayland-native"

SRCREV = "d62475b3fb9d0be07d7c5de9d108483dff88c631"
SRC_URI = "git://git.codelinaro.org/clo/la/wayland/wayland-ivi-extension.git;protocol=http;branch=upstream/master \
    "

S = "${WORKDIR}/git"

inherit cmake pkgconfig

FILES:${PN} += "\
        ${libdir}/weston/* \
        ${datadir}/wayland-protocols \
    "
FILES:${PN}-dbg += "${libdir}/weston/.debug/*"
