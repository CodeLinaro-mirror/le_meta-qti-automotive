SRC_URI:remove = "https://gstreamer.freedesktop.org/src/gst-plugins-ugly/gst-plugins-ugly-${PV}.tar.xz"
SRC_URI:append = " ${PATH_TO_REPO}/gstreamer/gstreamer/.git;protocol=${PROTO};destsuffix=gstreamer/gstreamer;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/gstreamer/gstreamer/subprojects/gst-plugins-ugly"
