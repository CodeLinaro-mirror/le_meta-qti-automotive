SUMMARY = "Platform Audio Layer control plugin"
DESCRIPTION = "This is platform audio layer control plugin."
HOMEPAGE = "http://git.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS += "agm ar-acdbdata ar-osal ar-pal expat gsl-fe-noship spf system-media tinyalsa tinycompress"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/pal/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/pal;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/pal/plugins/controls"

inherit autotools pkgconfig

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
