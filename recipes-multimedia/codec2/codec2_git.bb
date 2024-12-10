SUMMARY = "Codec2 multimedia framework"
DESCRIPTION = "Codec2 is a codec framework introduced by Google. This is to eventually replace OpenMax-IL."
HOMEPAGE = "https://git.codelinaro.org/"
SECTION = "multimedia"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/frameworks/NOTICE;md5=a3fcbe20ea5ac731ed3aa15fe59ba20a"

DEPENDS += "\
    clang-native \
    display-commonsys-intf-linux \
    gbm \
    gbm-headers \
    libcutils \
    libdmabufheap \
    libstagefright-headers \
    libutils \
    virtual/kernel-headers \
    media-plugin-headers \
    system-core-headers \
"

SRC_URI = "${PATH_TO_REPO}/frameworks/.git;protocol=${PROTO};destsuffix=frameworks;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/frameworks/av/media/codec2"

inherit cmake

CXXFLAGS += "\
    -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel} \
    -I${STAGING_INCDIR}/drm \
    -I${STAGING_INCDIR}/ion_headers \
    -I${STAGING_INCDIR}/kernel-headers \
    -I${STAGING_INCDIR}/c++/ \
    -I${STAGING_INCDIR}/c++/${TARGET_SYS} \
"

EXTRA_OECMAKE += "\
    -DAGL_LINUX:BOOL=ON \
    ${@oe.utils.version_less_or_equal('${preferred-kernel}', '5.4', '', '-DSUPPORT_DMABUF_HEAP:BOOL=ON', d)} \
"
EXTRA_OECMAKE:append:quin-gvm-lemans = " -DLOAD_CORE_LIB:BOOL=ON"
EXTRA_OECMAKE:append:quin-gvm-monaco = " -DLOAD_CORE_LIB:BOOL=ON"

# default enable C2 service on all build targets
EXTRA_OECMAKE:append = " -DUSE_AGL_C2SERVICE:BOOL=ON"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

TOOLCHAIN = "clang"
