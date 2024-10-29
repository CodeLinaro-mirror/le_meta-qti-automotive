SUMMARY = "Vhost user library"
DESCRIPTION = "vhost user library implement the vhost user protocol"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/vhost-user-lib/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/vhost-user-lib;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/vhost-user-lib"

inherit cmake

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
