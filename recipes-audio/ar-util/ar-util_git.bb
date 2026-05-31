SUMMARY = "AudioReach Util library"
DESCRIPTION = "This is the library used to define public AudioReach util APIs for double linked list."
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "glib-2.0"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/args/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/args/ar_util;subpath=ar_util;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/args/ar_util"

inherit pkgconfig cmake

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
