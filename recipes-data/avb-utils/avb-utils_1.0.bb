SUMMARY = "avb-utils_1.0"
DESCRIPTION = "\
avb utils include share library libeavbfe.so, eAVB (Ethernet Audio Video Bridging) \
Front-End device is virtual device node, multi applications may operate with ioctl cmds.\
libeavbfe.so to encapsulate ioctl operations"

HOMEPAGE = "http://git.codelinaro.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://NOTICE;md5=b45eb38359bd16993272b40c311aa89f"

DEPENDS += "glib-2.0"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/avb-utils/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/avb-utils;usehead=1"

SRCREV = "${AUTOREV}"
TARGET_CC_ARCH += "${LDFLAGS}"
CFLAGS += "\
    -DUSE_GLIB \
    -I${STAGING_INCDIR}/glib-2.0 \
    -I${STAGING_LIBDIR}/glib-2.0/include \
    -I${STAGING_LIBDIR}/glib-2.0/glib \
"

S = "${WORKDIR}/vendor/qcom/opensource/avb-utils"

do_install() {
    install -d ${D}/${libdir}
    install -d ${D}/${includedir}
    install -m 644 ${S}/libeavbfe/*.so ${D}/${libdir}
    install -m 644 ${S}/libeavbfe/inc/*.h ${D}/${includedir}
}

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
