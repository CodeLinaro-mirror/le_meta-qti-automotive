DESCRIPTION = "DTBO image creation tool from Android"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += "dtc-native"

PROVIDES = "mkdtimg-native"

PR = "r1"

SRC_URI = "${CLO_LA_GIT}/platform/system/libufdt;protocol=https;branch=lv-blast-tools.lnx.1.0;destsuffix=system/libufdt"
SRCREV = "981c03825c6bb469e0fabeb3d0fd9279bbd8e087"

S = "${WORKDIR}/system/libufdt"

inherit autotools pkgconfig native
