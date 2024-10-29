SUMMARY = "Native media hardware headers for OPENMAX"
DESCRIPTION = "Provide native media hardware headers for OPENMAX, \
these headers are introduced by Android Open Source project, used for \
extended features of OPENMAX, e.g. HDRStaticInfo, HDR10PlusInfo, and \
AndroidNativeBuffers"
HOMEPAGE = "https://git.codelinaro.org"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/frameworks/NOTICE;md5=a3fcbe20ea5ac731ed3aa15fe59ba20a"

SRC_URI = "${PATH_TO_REPO}/frameworks/.git;protocol=${PROTO};destsuffix=frameworks;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/frameworks"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${includedir}/media/hardware
    install -d ${D}${includedir}/media/drm
    install -m 0644 ${S}/native/include/media/hardware/*.h -D ${D}${includedir}/media/hardware/
    install -m 0644 ${S}/native/include/media/drm/*.h -D ${D}${includedir}/media/drm/
}

ALLOW_EMPTY:${PN} = "1"
