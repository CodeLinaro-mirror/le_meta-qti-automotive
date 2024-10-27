SUMMARY = "Android libhardware library"
DESCRIPTION = "This library provides access to the Android libhardware HAL(Hardware Abstraction Layer)."
HOMEPAGE = "http://git.codelinaro.org/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=9645f39e9db895a4aa6e02cb57294595"

DEPENDS += "libcutils liblog libutils system-core-headers"

FILESPATH =+ "${AUTOSOURCES}:"
SRC_URI     =  "file://hardware/libhardware"
SRC_URI     += "https://git.codelinaro.org/clo/la/platform/hardware/libhardware/-/raw/keystone/p-keystone-qcom-release/include/hardware/gralloc1.h;downloadfilename=gralloc1.h;name=gralloc-h"

S = "${WORKDIR}/hardware/libhardware"
SRCREV = "${AUTOREV}"

inherit autotools pkgconfig

do_install:append () {
    # remove headers, use libhardware-headers
    rm -rf ${D}${includedir}/hardware/
}
