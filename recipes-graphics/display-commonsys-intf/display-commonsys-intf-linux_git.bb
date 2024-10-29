SUMMARY = "display commonsys intf Library"
DESCRIPTION = "Provide common display header files and libraries for \
other modules to use."
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "libcutils libhardware-headers liblog libutils virtual/kernel-headers"

PR = "r3"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/commonsys-intf/display/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/commonsys-intf/display;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/vendor/qcom/opensource/commonsys-intf/display"

inherit autotools pkgconfig

EXTRA_OECONF += "--with-sanitized-headers=${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}"

LDFLAGS += "-llog -lutils -lcutils"

CPPFLAGS += "-DTARGET_HEADLESS"
CPPFLAGS += "-DVENUS_COLOR_FORMAT"
CPPFLAGS += "-DPAGE_SIZE=4096"
CPPFLAGS += "-I${WORKDIR}/vendor/qcom/opensource/commonsys-intf/display/gralloc"
CPPFLAGS += "-I${WORKDIR}/vendor/qcom/opensource/commonsys-intf/display/libqdmetadata"
CPPFLAGS += "-I${WORKDIR}/vendor/qcom/opensource/commonsys-intf/display/include"

do_install:append() {
    install -d ${D}${includedir}
    install -m 644 ${S}/gralloc/*.h ${D}${includedir}
    install -m 644 ${S}/include/*.h ${D}${includedir}
}

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
