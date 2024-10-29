SUMMARY = "display Library"
DESCRIPTION = "Provide display HAL (Hardware Abstraction Layer) \
libraries. These libraries serves as an abstraction layer between \
physical hardware and software. They provide display driver interfaces, \
allowing program to communicate with the hardware."
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "display-commonsys-intf-linux \
            drm \
            gbm-headers \
            libdrm \
            libhardware \
            virtual/kernel-headers \
            system-core \
            ${@bb.utils.contains_any("PREFERRED_VERSION_linux-msm", '5.15 6.1', 'displaydlkm', '', d)} \
            ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'display-kernel-headers', '', d)} \
"

PR = "r8"

SRC_URI = "${PATH_TO_REPO}/display/display-hal/.git;protocol=${PROTO};destsuffix=display/display-hal;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/display/display-hal"

inherit autotools-brokensep pkgconfig

EXTRA_OECONF += "--with-sanitized-headers=${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}"
EXTRA_OECONF += "--enable-sdmhaldrm"

LDFLAGS += "-llog -lhardware -lutils -lcutils"

CPPFLAGS += "-DCOMPILE_DRM"
CPPFLAGS += "-DTARGET_HEADLESS"
CPPFLAGS += "-DVENUS_COLOR_FORMAT"
CPPFLAGS += "-DPAGE_SIZE=4096"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/libdrmutils"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/gpu_tonemapper"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/libqdutils"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/libqservice"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/sdm/include"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/include"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/libdebug"
CPPFLAGS += "-I${STAGING_INCDIR}/libdrm"

# fix for uapi msm_drm.h header file related compilation issue
CPPFLAGS += "-fno-operator-names"

# add display techpack headers
CPPFLAGS += "-I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}/display"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
