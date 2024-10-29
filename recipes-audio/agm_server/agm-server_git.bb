SUMMARY = "AGM Server Library"
DESCRIPTION = "This is the server library of AGM, based on Binder IPC."
HOMEPAGE = "http://git.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS += "\
    agm ar-osal ar-util binder glib-2.0 \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'gsl-fe-noship libuhab', 'gsl', d)} \
    libcutils libutils virtual/kernel-headers spf \
"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/agm/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/agm;usehead=1 \
           file://agm.service \
           "
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/agm/ipc/SwBinders/agm_server"

inherit autotools pkgconfig systemd useradd

SYSTEMD_SERVICE:${PN} = "agm.service"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "agm"
USERADD_PARAM:${PN} = "--no-create-home -g agm --shell /bin/false agm"

do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -m 0644 ${WORKDIR}/agm.service -D ${D}${systemd_unitdir}/system/agm.service
    fi
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

RDEPENDS:${PN} += "\
    ar-osal \
    ar-util \
    binder \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'gsl-fe-noship libuhab', 'gsl', d)} \
"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
