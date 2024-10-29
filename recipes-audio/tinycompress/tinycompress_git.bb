SUMMARY = "Tinycompress Library"
DESCRIPTION = "This is the tinycompress library, it provides pcm and mixer interfaces to client for various audio playback and capture use cases."
HOMEPAGE = "https://www.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS += "libcutils"

SRC_URI = "\
    git://git.codelinaro.org/clo/la/platform/external/tinycompress;protocol=https;branch=aosp-new/master \
    file://0001-tinycompress-support-tinycompress-build-for-Linux.patch \
"

SRCREV = "f690ffd9a988d9a71c7ae80b9260b0bba09e69ae"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
