SUMMARY = "QTI Video Element Stream Deliver plugin for GStreamer"
DESCRIPTION = "Gstreamer video deliver plugin which supports secure buffer sharing"
HOMEPAGE = "https://git.codelinaro.org/"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=b796c0007db682166a1721da80267bb2"

DEPENDS += "\
    glib-2.0 \
    gstreamer1.0 \
    gstreamer1.0-plugins-base \
    virtual/kernel-headers \
    ${@oe.utils.version_less_or_equal('${preferred-kernel}', '5.4', '', 'libdmabufheap libvmmem-headers', d)} \
"

SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-qti-oss/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-qti-oss;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/gst-plugin-vesdeliver"

inherit meson pkgconfig

CFLAGS += "\
    -I${STAGING_INCDIR}/glib-2.0 \
    -I${STAGING_LIBDIR}/glib-2.0/include \
    -I${STAGING_INCDIR}/glib-2.0/glib \
    -I${STAGING_INCDIR}/gstreamer-1.0 \
    -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel} \
    -I${STAGING_INCDIR}/${@oe.utils.version_less_or_equal('${preferred-kernel}', '5.4', 'ion_headers', 'vmmem', d)} \
"

EXTRA_OEMESON += "\
    ${@oe.utils.version_less_or_equal('${preferred-kernel}', '5.4', '', '-Dusedmaheap=true', d)} \
"

EXTRA_OEMESON += "\
    ${@oe.utils.version_less_or_equal('${preferred-kernel}', '5.16', '', '-Dcontiguous_mem_option=op1 -Ddisable_secure_copy=true', d)} \
"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

FILES:${PN} += "${libdir}/gstreamer-1.0/*.so"

RDEPENDS:${PN} += "${@oe.utils.version_less_or_equal('${preferred-kernel}', '5.4', 'libion', 'libdmabufheap libvmmem', d)}"
