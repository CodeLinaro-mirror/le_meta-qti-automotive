SUMMARY = "Generic Packet Router"
DESCRIPTION = "This is is a layer which route packet from GSL to SPF."
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "ar-osal glib-2.0 libglink-client"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/args/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/args/gpr;subpath=gpr;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/args/gpr"

inherit pkgconfig cmake

EXTRA_OECONF += "--with-glib"

EXTRA_OECMAKE:append:gen5 = " -DPLATFORM_LY=ON"

RDEPENDS:${PN} += "glink-service-lrm"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
