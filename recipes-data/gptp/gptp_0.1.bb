SUMMARY = "GPTP"
DESCRIPTION = "Time Sensitive Networking stack Time Sync"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "\
    glib-2.0 \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'libuhab', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'ptp-vk', '', d)} \
"

SRC_URI = "\
    ${PATH_TO_REPO}/external/open-avb/.git;protocol=${PROTO};destsuffix=external/open-avb;usehead=1 \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/external/open-avb"

inherit systemd pkgconfig useradd autotools-brokensep

# Add non-root user vnw for gptp-daemon.service
USERADD_PACKAGES = "${PN}"

USERADD_PARAM:${PN} = "--no-create-home --shell /bin/false -g vnw vnw"
GROUPADD_PARAM:${PN} = "net_raw; net_admin; vnw;"

PACKAGE_ARCH = "${MACHINE_ARCH}"

TARGET_CC_ARCH += "${LDFLAGS}"

EXTRA_OEMAKE += "ENABLE_GPTP=1"
EXTRA_OEMAKE += "ENABLE_LIBGPTP=1"
EXTRA_OEMAKE += "ENABLE_LIBGPTP_TEST=1"
EXTRA_OEMAKE += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '', 'ENABLE_GPTP_SERVICE=1', d)}"
EXTRA_OEMAKE += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'AVB_FEATURE_GVM_MODE=1', '', d)}"
#EXTRA_OEMAKE += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'GPTP_VFIO=1', '', d)}"
SYSTEMD_SERVICE:${PN} = "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '', 'gptp.service', d)}"

do_compile() {
    oe_runmake gptp
    oe_runmake libgptp
    oe_runmake libgptp_test
}

PACKAGES =+ "${PN}-test"

RDEPENDS:${PN} += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'libuhab', '', d)}"
RDEPENDS:${PN}-test += "${PN}"

CXXFLAGS += "-I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}"
CFLAGS += "-I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
FILES:${PN} += "${libdir}/*"
FILES:${PN} += "${bindir}/*"
FILES:${PN} += "${sysconfdir}/*"
FILES:${PN} += "${systemd_unitdir}/*"
