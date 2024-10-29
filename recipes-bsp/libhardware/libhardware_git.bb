SUMMARY = "Android libhardware library"
DESCRIPTION = "This library provides access to the Android libhardware HAL(Hardware Abstraction Layer)."
HOMEPAGE = "http://git.codelinaro.org/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=9645f39e9db895a4aa6e02cb57294595"

DEPENDS += "libcutils liblog libutils system-core-headers"

SRC_URI = "\
    ${PATH_TO_REPO}/hardware/libhardware/.git;protocol=${PROTO};destsuffix=hardware/libhardware;usehead=1 \
    https://git.codelinaro.org/clo/la/platform/hardware/libhardware/-/raw/keystone/p-keystone-qcom-release/include/hardware/gralloc1.h;downloadfilename=gralloc1.h;name=gralloc-h \
"
SRC_URI[gralloc-h.sha256sum] = "19e9f8acac6ab89d8ec11aefa1e6e0aa6ca49b73f2c6fd17cb7bc487b5841ee6"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/hardware/libhardware"

inherit autotools pkgconfig

do_install:append () {
    # remove headers, use libhardware-headers
    rm -rf ${D}${includedir}/hardware/
}
