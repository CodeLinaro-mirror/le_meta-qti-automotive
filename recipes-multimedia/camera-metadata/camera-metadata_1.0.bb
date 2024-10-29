SUMMARY = "camera-metadata"
DESCRIPTION = "Recipe to provide Camera Metadata library"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += "libcutils"

SRC_URI = "\
    ${PATH_TO_REPO}/frameworks/.git;protocol=${PROTO};destsuffix=frameworks;usehead=1 \
"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/frameworks/camera_metadata"

inherit autotools pkgconfig

FILES:${PN}-dbg += "${libdir}/.debug/lib*.*"
FILES:${PN} += "${libdir}/lib*.so.* ${libdir}/pkgconfig/*"
FILES:${PN}-dev += "${libdir}/lib*.so ${libdir}/lib*.la"
