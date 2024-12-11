SUMMARY = "QTI DRM decryptor plugin for GStreamer"
DESCRIPTION = "GStreamer plugin for decrypting DRM content with support for PlayReady and Widevine"
HOMEPAGE = "https://git.codelinaro.org/"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=b796c0007db682166a1721da80267bb2"

DEPENDS += "\
    glib-2.0 \
    gstreamer1.0 \
    gstreamer1.0-plugins-base \
    virtual/kernel-headers \
    media-plugin-headers \
    libstagefright-headers \
    libutils \
    libcutils \
"

SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-qti-oss/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-qti-oss;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/gst-plugin-drmdecryptor"

inherit meson pkgconfig

CXXFLAGS += "\
    -I${STAGING_INCDIR}/glib-2.0 \
    -I${STAGING_INCDIR}/gstreamer-1.0 \
    -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel} \
    -I${STAGING_INCDIR}/${@oe.utils.version_less_or_equal('${preferred-kernel}', '5.4', 'ion_headers', '', d)} \
"

EXTRA_OEMESON += "\
    ${@oe.utils.version_less_or_equal('${preferred-kernel}', '5.4', '', '-Dusedmaheap=true', d)} \
"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

FILES:${PN} += "${libdir}/gstreamer-1.0/*.so"

RDEPENDS:${PN} += "${@oe.utils.version_less_or_equal('${preferred-kernel}', '5.4', 'libion', 'libdmabufheap', d)}"
