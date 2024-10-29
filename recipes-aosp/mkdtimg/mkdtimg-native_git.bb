DESCRIPTION = "DTBO image creation tool from Android"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += "dtc-native"

PROVIDES = "mkdtimg-native"

PR = "r1"

SRC_URI = "${PATH_TO_REPO}/system/libufdt/.git;protocol=${PROTO};destsuffix=system/libufdt;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/system/libufdt"

inherit autotools pkgconfig native
