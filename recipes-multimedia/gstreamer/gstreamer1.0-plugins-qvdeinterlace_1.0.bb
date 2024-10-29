SUMMARY = "QTI Video Deinterlace plugin for GStreamer"
DESCRIPTION = "Gstreamer video deinterlace plugin based on GPU hardware deinterlace"
HOMEPAGE = "https://git.codelinaro.org/"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=b796c0007db682166a1721da80267bb2"

DEPENDS += "\
    adreno \
    display-commonsys-intf-linux \
    gbm \
    gbm-headers \
    glib-2.0 \
    gstreamer1.0 \
    gstreamer1.0-plugins-base \
    virtual/kernel-headers \
    mm-gfx-auto-prop \
    videodlkm \
"

DEPENDS:append:quin-gvm-lemans = " displaydlkm"
DEPENDS:append:quin-gvm-monaco = " displaydlkm"

SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-qti-oss/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-qti-oss;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/gst-plugin-qvdeinterlace"

inherit meson pkgconfig

CFLAGS += "-I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}"

CFLAGS:append:quin-gvm-lemans = " -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}/display"
EXTRA_OEMESON:append:quin-gvm-lemans = " \
    -Dmmmcolorfmt=true \
"

CFLAGS:append:quin-gvm-monaco = " -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}/display"
EXTRA_OEMESON:append:quin-gvm-monaco = " \
     -Dmmmcolorfmt=true \
"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

FILES:${PN} += "${libdir}/gstreamer-1.0/*.so"
RDEPENDS:${PN} += "mm-gfx-auto-prop"
