SUMMARY = "Audio Graph Manager"
DESCRIPTION = "This is the audio graph manager (AGM) service, it provides interfaces to allow mixer control and pcm plugins to interact and enable verious audio usecases."
HOMEPAGE = "http://git.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS += "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'qti-dpk', '', 'alsa-lib', d)} \
    ar-acdbdata ar-osal ar-util glib-2.0 \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'gsl-fe-noship libuhab', 'ats gsl', d)} \
    virtual/kernel-headers mm-audio-headers spf \
    ${@bb.utils.contains('DISTRO_FEATURES', 'qti-dpk', 'tinyalsa', '', d)} \
"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/agm/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/agm;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/agm/service"

inherit autotools pkgconfig

EXTRA_OECONF += "\
    --with-glib \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '--enable-target-hypervisor', '', d)} \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"

RDEPENDS:${PN} += "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'qti-dpk', '', 'alsa-lib', d)} \
    ar-acdbdata \
    ar-util \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'gsl-fe-noship libuhab', 'gsl', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'qti-dpk', 'tinyalsa', '', d)} \
"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
