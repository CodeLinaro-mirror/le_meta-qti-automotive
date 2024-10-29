SUMMARY = "QTI Screen shot SRC Plugin for GStreamer"
DESCRIPTION = "QTI screen shot plugin for Gstreamer. It captures your Display and creates raw RGBA8888 video"
HOMEPAGE = "https://git.codelinaro.org"
SECTION = "multimedia"
LICENSE = "LGPL-2.0 & BSD-3-Clause & MIT"
LIC_FILES_CHKSUM = "file://NOTICE.txt;md5=75cfdd159cf288058ddf776b13ec384c"

DEPENDS += "\
    glib-2.0 \
    gstreamer1.0 \
    gstreamer1.0-plugins-bad \
    gstreamer1.0-plugins-base \
    virtual/kernel-headers \
    ${@bb.utils.contains("PREFERRED_VERSION_linux-msm", "5.15", "videodlkm", "", d)} \
    virtual/libc \
    wayland-native \
    weston \
"

PR = "r1"

SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-qti-oss/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-qti-oss;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/gst-plugin-qscreencapsrc"

inherit autotools-brokensep pkgconfig

CFLAGS += "\
    -I${STAGING_INCDIR} \
    -I${STAGING_INCDIR}/drm \
    -I${STAGING_INCDIR}/weston \
    -I${STAGING_INCDIR}/pixman-1 \
    -I${STAGING_INCDIR}/../lib64/glib-2.0/include \
    -I${STAGING_INCDIR}/glib-2.0 \
    -I${STAGING_INCDIR}/glib-2.0/include \
    -I${STAGING_INCDIR}/glib-2.0/glib \
    -I${STAGING_INCDIR}/c++ \
    -I${STAGING_INCDIR}/c++/${TARGET_SYS} \
    -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel} \
    -DUSE_V6 \
"

FILES:${PN} += "${libdir}/gstreamer-1.0/*.so"
