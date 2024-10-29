SUMMARY = "QTI DRM player example for GStreamer"
DESCRIPTION = "QTI DRM player example for GStreamer with support for PlayReady and Widevine"
HOMEPAGE = "https://git.codelinaro.org"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=b796c0007db682166a1721da80267bb2"

DEPENDS += "\
    glib-2.0 \
    gstreamer1.0 \
    gstreamer1.0-plugins-base \
    libxml2 \
    curl \
    media-plugin-headers \
    libstagefright-headers \
    libutils \
"

SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-qti-oss/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-qti-oss;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/gst-plugin-examples"

inherit cmake pkgconfig

CXXFLAGS += "\
    -I${STAGING_INCDIR}/glib-2.0 \
    -I${STAGING_INCDIR}/gstreamer-1.0 \
    -I${STAGING_INCDIR}/libxml2 \
"
