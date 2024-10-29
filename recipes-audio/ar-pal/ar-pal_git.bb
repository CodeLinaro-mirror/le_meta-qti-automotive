SUMMARY = "Platform Audio Layer"
DESCRIPTION = "This is platform audio layer, it provides interfaces to enable various audio use cases."
HOMEPAGE = "http://git.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS += "agm agm-client ar-acdbdata ar-osal expat glib-2.0 gsl-fe-noship mm-audio-headers spf system-media tinyalsa tinycompress"

SRC_URI = "\
    ${PATH_TO_REPO}/vendor/qcom/opensource/pal/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/pal;usehead=1 \
    file://mixer_paths-dpk.xml \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/pal"

inherit autotools pkgconfig

EXTRA_OECONF += "\
    --with-glib \
    --with-spf=${STAGING_INCDIR}/spf \
    --with-acdbdata=${STAGING_INCDIR}/acdbdata \
"

MIXER_PATHS_XML ?= "mixer_paths_gvmauto8295_adp_star.xml"
MIXER_PATHS_XML:quin-gvm-lemans = "mixer_paths_gvmauto8255_adp_star.xml"
MIXER_PATHS_XML:quin-gvm-monaco = "mixer_paths_gvmauto7255_adp_star.xml"

do_install:append() {
    install -d ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/mixer_paths-dpk.xml ${D}${sysconfdir}/${MIXER_PATHS_XML}
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
