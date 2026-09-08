SUMMARY = "Video external buffer pool sink plugin for GStreamer"
DESCRIPTION = "Gstreamer video sink plugin to provide external buffer pool"
HOMEPAGE = "https://git.codelinaro.org/"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=b796c0007db682166a1721da80267bb2"

DEPENDS += "\
    display-commonsys-intf-linux \
    gbm \
    gbm-headers \
    glib-2.0 \
    gstreamer1.0 \
    gstreamer1.0-plugins-base \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', '', 'videodlkm displaydlkm', d)} \
    virtual/kernel-headers \
"

SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-qti-oss/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-qti-oss;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/gst-plugin-extpoolsink"

inherit meson pkgconfig

CFLAGS += "-I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}"

CFLAGS:append:gvm-gen4-5 = " -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}/display"
CFLAGS:append:gvm-gen5 = " -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}/display"

EXTRA_OEMESON:append = " \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', '-Duseumd=true', '', d)} \
"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""

FILES:${PN} += "${libdir}/gstreamer-1.0/*.so"
