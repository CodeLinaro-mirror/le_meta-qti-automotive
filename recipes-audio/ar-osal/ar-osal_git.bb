SUMMARY = "AudioReach OSAL (Operating System Abstracting Layer)"
DESCRIPTION = "This is the AudioReach OSAL library which provides task scheduling, time management and other functions for AudioReach."
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"
# Downstream product configuration may set this to "0" for legacy syslog.
AR_OSAL_AUDIO_LOG_UTIL_BRIDGE ?= "1"

# ar-osal has no direct dependency on qmi-framework actually. ss-services depends on qmi-framework.
# In HY11 build environment, ar-osal may not get the headers from qmi-framework if it just
# depends on ss-services. The dependency chain will break at ss-service because it is bin-released.
DEPENDS += "glib-2.0 libcutils audio-headers-export audio-utils ar-util libkiumd diag-lsm virtual/kernel-headers safelinux-cfg-modules system-core common-headers"
DEPENDS:append = " ${@bb.utils.contains('AR_OSAL_AUDIO_LOG_UTIL_BRIDGE', '1', 'audio-log-util', '', d)}"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/args/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/args/ar_osal;subpath=ar_osal;usehead=1"
SRCREV = "${AUTOREV}"

RDEPENDS:${PN}:append = " ${@bb.utils.contains('AR_OSAL_AUDIO_LOG_UTIL_BRIDGE', '1', 'audio-log-util', '', d)}"

S = "${WORKDIR}/vendor/qcom/opensource/args/ar_osal"

inherit pkgconfig cmake

EXTRA_OECONF += "\
    --with-glib \
    --with-sanitized-headers=${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel} \
    with_libion=no \
"

EXTRA_OECMAKE:append:gen5 = " -DPLATFORM_LY=ON"
EXTRA_OECMAKE:append = " -DAR_OSAL_AUDIO_LOG_UTIL_BRIDGE=${@bb.utils.contains('AR_OSAL_AUDIO_LOG_UTIL_BRIDGE', '1', 'ON', 'OFF', d)}"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
PACKAGE_ARCH = "${MACHINE_ARCH}"
