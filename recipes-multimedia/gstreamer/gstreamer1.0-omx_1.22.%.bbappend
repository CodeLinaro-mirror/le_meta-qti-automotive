DEPENDS += "virtual/kernel-headers media"
DEPENDS:remove = "virtual/libomxil"

SRC_URI:remove = "https://gstreamer.freedesktop.org/src/gst-omx/gst-omx-${PV}.tar.xz"
SRC_URI:append = " ${PATH_TO_REPO}/gstreamer/gstreamer/.git;protocol=${PROTO};destsuffix=gstreamer/gstreamer;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/gstreamer/gstreamer/subprojects/gst-omx"

GSTREAMER_1_0_OMX_TARGET = "generic"
GSTREAMER_1_0_OMX_CORE_NAME = "${libdir}/libOmxCore.so"

EXTRA_OEMESON = "\
                 -Dtarget=qti \
                 -Dheader_path=${STAGING_INCDIR}/mm-core \
                 -Dkernel_path=${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel} \
                 -Dstaging_inc_path=${STAGING_INCDIR} \
                "
EXTRA_OEMESON:append:sa6155 = " -Denable-target-6155=yes"

RDEPENDS:${PN} = "media"
