SUMMARY = "avb (audio video bridge) plugins for GStreamer"
DESCRIPTION = "avb (audio video bridge) plugins for GStreamer, receive pcm data or ts data from qavb FE, push the data to downstream to support audio or video playback"
HOMEPAGE = "https://git.codelinaro.org/"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://NOTICE.txt;md5=a2bd2b12cb1c303823d7af2d87a3ba8d"

DEPENDS += "avb-utils \
            glib-2.0 \
            gstreamer1.0 \
            gstreamer1.0-plugins-base \
            virtual/kernel-headers \
            virtual/libc \
           "

PR = "r1"

SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-qti-oss/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-qti-oss;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/gst-plugins-qeavb"

inherit meson pkgconfig

FILES:${PN} += "${libdir}/gstreamer-1.0/*.so"
FILES:${PN}-dbg += "${libdir}/gstreamer-1.0/.debug"
FILES:${PN}-dev += "${libdir}/gstreamer-1.0/*.la"

CFLAGS += "-I${STAGING_INCDIR} \
           -I${STAGING_INCDIR}/glib-2.0 \
           -I${STAGING_INCDIR}/../lib/glib-2.0/include \
           -I${STAGING_INCDIR}/glib-2.0/include \
           -I${STAGING_INCDIR}/glib-2.0/glib \
           -I${STAGING_INCDIR}/c++ \
           -I${STAGING_INCDIR}/c++/${TARGET_SYS} \
           -I${STAGING_INCDIR}/gstreamer-1.0 \
           -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel} \
           ${@oe.utils.version_less_or_equal('${preferred-kernel}', '5.16', '', '-DKPI_USE_SYSLOG', d)} \
          "

RDEPENDS:${PN} += "avb-utils"
