SUMMARY = "Android core component headers"
DESCRIPTION = "The system/core directory is intended for pieces of the world that are \
the core of the embedded linux platform at the heart of Android. These essential bits \
are required for basic booting, operation, and debugging."
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=c1a3ff0b97f199c7ebcfdd4d3fed238e"

SRC_URI = "${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=system/core;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/system/core"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
    # export head files
    install -d ${D}${includedir}/system
    install -m 0644 ${S}/include/system/camera.h  ${D}${includedir}/system/
    install -m 0644 ${S}/include/system/graphics.h  ${D}${includedir}/system/
    install -m 0644 ${S}/include/system/thread_defs.h  ${D}${includedir}/system/
    install -m 0644 ${S}/include/system/window.h  ${D}${includedir}/system/

    install -d ${D}${includedir}/sys
    install -m 0644 ${S}/include/sys/system_properties.h  ${D}${includedir}/sys/

    install -d ${D}${includedir}/netutils
    install -m 0644 ${S}/include/netutils/dhcp.h  ${D}${includedir}/netutils/
    install -m 0644 ${S}/include/netutils/ifc.h  ${D}${includedir}/netutils/
}
