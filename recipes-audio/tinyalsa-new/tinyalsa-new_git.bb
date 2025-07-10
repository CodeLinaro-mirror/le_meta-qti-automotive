SUMMARY = "Tinyalsa Library"
DESCRIPTION = "This is the tinyalsa library, it provides pcm and mixer interfaces to client for various audio playback and capture use cases."
HOMEPAGE = "https://github.com/tinyalsa/tinyalsa"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS += "libcutils"

SRC_URI = "\
    git://git.codelinaro.org/clo/la/platform/external/tinyalsa_new;protocol=https;branch=aosp-new/master \
    file://0001-tinyalsa-Fix-interger-comparison-warning.patch \
    file://0001-Enable-TinyAlsa-plugin-support.patch \
    file://0001-Call-pcm_start-for-capture-case-in-NON-MMAP-mode.patch \
    file://0001-add-tinyhostless-for-tinyalsa-new.patch \
    file://0001-utils-tinycap-support-8bits-record.patch \
    file://0001-utils-tinyplay-check-fopen-return-value-to-prevent-s.patch \
"

SRCREV = "f78ed25aced2dfea743867b8205a787bfb091340"

S = "${WORKDIR}/git"

inherit cmake pkgconfig
