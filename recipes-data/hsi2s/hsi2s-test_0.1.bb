SUMMARY = "hsi2s-test"
DESCRIPTION = "Recipe to generate the HS-I2S test application. The application interacts with the HS-I2S driver using IOCTLs on the exposed device nodes for each interface. It is used to dump the data received on the HS-I2S interfaces into an output file. It also supports various test modes."
HOMEPAGE = "https://git.codelinaro.org/"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://../../NOTICE;md5=434b8411d18d7f18ebe745bd3cc502ed"

PROVIDES = "hsi2s-test"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/hsi2s-kernel/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/hsi2s-kernel;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/hsi2s-kernel/test/generic"

inherit autotools-brokensep

TARGET_CC_ARCH += "${LDFLAGS}"

do_compile () {
    oe_runmake -C ${S}
}

do_install () {
    install -d ${D}${bindir}
    install -m 0755 hsi2s_test ${D}${bindir}
}
