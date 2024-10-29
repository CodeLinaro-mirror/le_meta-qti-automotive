SUMMARY = "Unified driver for Aurix CAN controllers"
DESCRIPTION = "This adds unified driver for Aurix CAN controllers on auto Platform. This driver can support a few Uart controllers being used on automotive paltform. This Driver uses uart bus to comunicate with Aurix can."
HOMEPAGE = "https://git.codelinaro.org/"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=dad791a35899eec116e1ca77e51abaa7"

DEPENDS += "virtual/kernel"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/aurix-can-kernel/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/aurix-can-kernel;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/aurix-can-kernel"

inherit module module-sign kernel-arch qperf ${@oe.utils.ifelse(d.getVar('PREFERRED_PROVIDER_virtual/kernel') == 'linux-msm',"qti-kernel-arch-clang", "")}
INHIBIT_PACKAGE_STRIP = "1"

PACKAGE_ARCH = "${MACHINE_ARCH}"

EXTRA_OEMAKE += "CONFIG_ARCH_MSM=y"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
FILES_${PN} += "${sysconfdir}/*"

RPROVIDES:${PN} += "${@'kernel-module-aurix-can-${KERNEL_VERSION}'.replace('_', '-')}"
