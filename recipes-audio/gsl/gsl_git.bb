SUMMARY = "Graph Service Layer"
DESCRIPTION = "This is the library used to setup and configure the graph in SPF framework."
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"
DEPENDS += "acdb ar-util glib-2.0 gpr spf"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/args/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/args/gsl;subpath=gsl;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/args/gsl"

inherit pkgconfig cmake

EXTRA_OECONF += "--with-glib"

RDEPENDS:${PN} += "ssr-rm fastrpc-rm ar-osal"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
