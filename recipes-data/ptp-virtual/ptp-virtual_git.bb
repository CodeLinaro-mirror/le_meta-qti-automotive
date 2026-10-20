SUMMARY = "implement the Precision Time Protocol(ptp) virtual driver"
DESCRIPTION = "Precision Time Protocol(ptp) virtual driver leverage the share memory machanism to sync the gptp timestamp between Host and GVM"
HOMEPAGE = "https://git.codelinaro.org/"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://NOTICE;md5=752b838e10ae75e6f917015849cf56b0"

DEPENDS += "virtual/kernel"
do_configure[depends] += "virtual/kernel:do_shared_workdir"

SRC_URI = "\
    ${PATH_TO_REPO}/vendor/qcom/opensource/ptp-virtual/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/ptp-virtual;usehead=1 \
    file://ptp_virtual_load.conf \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/ptp-virtual"

inherit module module-sign kernel-arch qperf qti-kernel-arch-clang
INHIBIT_PACKAGE_STRIP = "1"
EXTRA_OEMAKE += "CONFIG_ARCH_MSM=y"

do_install:append() {
    install -m 0755 ${WORKDIR}/ptp_virtual_load.conf -D ${D}${sysconfdir}/modules-load.d/ptp_virtual_load.conf
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILES:${PN} += "\
    ${sysconfdir}/* \
    ${nonarch_base_libdir}/modules/${KERNEL_VERSION}/* \
"

RPROVIDES:${PN} += "kernel-module-ptp-virtual-${KERNEL_VERSION}"

# lock to avoid parallel compiling with techpack
do_compile[lockfiles] += "${TMPDIR}/qti-techpack.lock"