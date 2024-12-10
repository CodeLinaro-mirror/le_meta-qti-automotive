SUMMARY = "QTI camera source plugin for GStreamer"
DESCRIPTION = "qcarcamsrc is a source plugin to fetch raw yuv data from qcx"
HOMEPAGE = "https://git.codelinaro.org/"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause-Clear & BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/BSD-3-Clause-Clear;md5=b796c0007db682166a1721da80267bb2 \
                    file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
                   "
DEPENDS += "\
    gbm \
    gbm-headers \
    gstreamer1.0 \
    gstreamer1.0-plugins-base \
    camera-qcx \
"

SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-qti-oss/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-qti-oss;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/gst-plugin-qcarcamsrc"

inherit meson pkgconfig

CFLAGS += "-I${STAGING_INCDIR} \
           -I${STAGING_INCDIR}/gstreamer-1.0 \
           -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}/display \
          "

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

FILES:${PN} += "${libdir}/gstreamer-1.0/*.so"
