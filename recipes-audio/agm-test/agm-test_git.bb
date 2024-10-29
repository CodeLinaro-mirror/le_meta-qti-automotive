SUMMARY = "AGM Test Library"
DESCRIPTION = "Audio playback and capture testing library in AGM"
HOMEPAGE = "http://git.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS += "agm tinycompress expat"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/agm/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/agm;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/agm/plugins/tinyalsa/test"

inherit autotools pkgconfig

PACKAGE_ARCH = "${MACHINE_ARCH}"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
