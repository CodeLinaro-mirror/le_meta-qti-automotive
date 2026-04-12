SUMMARY = "Signal Processing Framework (SPF)"
DESCRIPTION = "This is the Signal Processing Framework header files recipe"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/args/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/args/spf;subpath=spf;usehead=1"
SRCREV = "${AUTOREV}"

SRC_DIR = "${SRC_DIR_ROOT}/vendor/qcom/opensource/args/spf"
S = "${WORKDIR}/vendor/qcom/opensource/args/spf"

inherit pkgconfig cmake

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
