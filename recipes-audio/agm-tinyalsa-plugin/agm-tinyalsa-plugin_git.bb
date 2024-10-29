SUMMARY = "AGM Tinyalsa Plugin Library"
DESCRIPTION = "This is the AGM tinyalsa plugin to support tinyalsa lib APIs."
HOMEPAGE = "http://git.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS += "agm-client agm-sndparser ar-osal ar-util audio-log-utils gsl-fe-noship libuhab mm-audio-headers spf tinyalsa tinycompress"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/agm/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/agm;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/agm/plugins/tinyalsa"

inherit autotools pkgconfig

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
