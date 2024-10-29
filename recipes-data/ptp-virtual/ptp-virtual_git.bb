SUMMARY = "implement the Precision Time Protocol(ptp) virtual driver"
DESCRIPTION = "Precision Time Protocol(ptp) virtual driver leverage the share memory machanism to sync the gptp timestamp between Host and GVM"
HOMEPAGE = "https://git.codelinaro.org/"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=752b838e10ae75e6f917015849cf56b0"

DEPENDS += "virtual/kernel"

SRC_URI = "\
    ${PATH_TO_REPO}/vendor/qcom/opensource/ptp-virtual/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/ptp-virtual;usehead=1 \
    file://ptp-virtual.service \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/ptp-virtual"

inherit module module-sign kernel-arch qperf qti-kernel-arch-clang systemd useradd
INHIBIT_PACKAGE_STRIP = "1"
EXTRA_OEMAKE += "CONFIG_ARCH_MSM=y"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "vnw"
USERADD_PARAM:${PN} = "--no-create-home -g vnw --shell /bin/false vnw"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/ptp-virtual.service ${D}${systemd_unitdir}/system/ptp-virtual.service
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILES:${PN} += "\
    ${systemd_unitdir}/system/ptp-virtual.service \
    ${nonarch_base_libdir}/modules/${KERNEL_VERSION}/* \
"

RPROVIDES:${PN} += "kernel-module-ptp-virtual-${KERNEL_VERSION}"
