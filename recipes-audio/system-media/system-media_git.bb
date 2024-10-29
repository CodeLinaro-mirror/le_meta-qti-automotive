SUMMARY = "System Media Libraries"
DESCRIPTION = "This it the system media libraries, used by audio hal to control sound card, set mixer path, etc."
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += "expat libutils tinyalsa"

PROVIDES += "audio-route audio-utils audio-effects"

SRC_URI = "${PATH_TO_REPO}/frameworks/.git;protocol=${PROTO};destsuffix=frameworks;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/frameworks/system/media"

inherit autotools pkgconfig
