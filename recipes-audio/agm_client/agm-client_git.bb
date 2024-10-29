SUMMARY = "AGM Client Library"
DESCRIPTION = "This is the client library of AGM, based on Binder IPC."
HOMEPAGE = "http://git.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS += "\
    agm-server ar-osal ar-util binder \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'gsl-fe-noship libuhab', 'gsl', d)} \
    libcutils liblog libutils virtual/kernel-headers spf \
"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/agm/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/agm;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/agm/ipc/SwBinders/agm_client"

inherit autotools pkgconfig

PACKAGE_ARCH = "${MACHINE_ARCH}"

RDEPENDS:${PN} += "\
    agm-server \
    ar-osal \
    ar-util \
    binder \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'gsl-fe-noship libuhab', 'gsl', d)} \
"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
