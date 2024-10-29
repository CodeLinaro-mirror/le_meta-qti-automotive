SUMMARY = "AGM snd_card parser"
DESCRIPTION = "This is the snd_card parser to get the card defination for AGM."
HOMEPAGE = "http://git.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS += "\
    agm ar-osal ar-util expat glib-2.0 \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'gsl-fe-noship libuhab', 'gsl', d)} \
    libcutils virtual/kernel-headers spf \
"

SRC_URI = "\
    ${PATH_TO_REPO}/vendor/qcom/opensource/agm/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/agm;usehead=1 \
    file://card-defs-dpk.xml \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/agm/snd_parser"

inherit autotools pkgconfig

EXTRA_OECONF += "--with-glib"

do_install:append() {
    install -d ${D}${sysconfdir}
    install -m 0644 ${S}/configs/sa8295/card-defs.xml ${D}${sysconfdir}/card-defs.xml
}

do_install:append:qti-dpk() {
    install -m 0644 ${WORKDIR}/card-defs-dpk.xml ${D}${sysconfdir}/card-defs.xml
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

RDEPENDS:${PN} += "\
    ar-osal \
    ar-util \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'gsl-fe-noship libuhab', 'gsl', d)} \
"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
