SUMMARY = "Audio Calibration Database"
DESCRIPTION = "This is a library of the Audio Calibration Database (ACDB) module."
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"
DEPENDS += "ar-osal"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/args/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/args/acdb;subpath=acdb;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/args/acdb"
SRC_DIR = "${WORKDIR}/vendor/qcom/opensource/args/acdb"

inherit pkgconfig cmake qprebuilt

RDEPENDS:${PN} += "ar-osal"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
