SUMMARY = "Tinyalsa Library"
DESCRIPTION = "This is the tinyalsa library, it provides pcm and mixer interfaces to client for various audio playback and capture use cases."
HOMEPAGE = "https://www.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS += "libcutils"

SRC_URI = "\
    git://git.codelinaro.org/clo/la/platform/external/tinyalsa;protocol=https;branch=aosp-new/master \
    file://0001-tinyalsa-support-tinyalsa-build-for-Linux.patch \
"
SRCREV = "61bf563f1df205cf14df142721aaafb854bbbd82"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
