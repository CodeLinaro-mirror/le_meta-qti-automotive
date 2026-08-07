SUMMARY = "AudioReach Transport Service"
DESCRIPTION = "This is the library used to transport service to QACT."
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "\
            acdb ar-osal glib-2.0 gsl diag \
"
SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/args/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/args/acdb/ats;subpath=ats;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/vendor/qcom/opensource/args/acdb/ats"

inherit pkgconfig cmake

EXTRA_OECONF += "\
    --with-libdiag=yes \
    --with-diag=${STAGING_INCDIR}/diag \
    --with-glib \
"

RDEPENDS:${PN} += "\
    ar-osal \
    diagservice diag-lsm \
"

EXTRA_OECMAKE:append:gen5 = " -DPLATFORM_LY=ON"

CFLAGS += "-I${STAGING_INCDIR}/diag_lsm/include"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
PACKAGE_ARCH = "${MACHINE_ARCH}"
