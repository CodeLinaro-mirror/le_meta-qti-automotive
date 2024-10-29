SUMMARY = "HS-I2S driver"
DESCRIPTION = "Recipe to generate the HS-I2S driver module. The driver configures the HS-I2S interfaces in the audio subsystem. It is typically used to receive high speed I2S data from radio tuners for Software Defined Radio(SDR) applications."
HOMEPAGE = "https://git.codelinaro.org/"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=434b8411d18d7f18ebe745bd3cc502ed"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/hsi2s-kernel/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/hsi2s-kernel;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/hsi2s-kernel"

TECHPACK_MODULE_OUT = "${WORKDIR}/vendor/qcom/opensource/hsi2s-kernel"
TECHPACK_MODULES = "${@bb.utils.contains('BASEMACHINE', 'sa81x5', 'hsi2s.ko', 'driver/hsi2s.ko', d)}"

inherit qti-techpack module module-sign ${@oe.utils.ifelse(d.getVar('PREFERRED_PROVIDER_virtual/kernel') == 'linux-msm',"qti-kernel-arch-clang", "")}

TECHPACK_MAKE_ARGS = "${@bb.utils.contains_any('PREFERRED_VERSION_linux-msm', '5.15 6.1', "${EXTRA_OEMAKE} QTI_TECHPACK=true", "", d)} LEGACY_PATH="${S}""

INHIBIT_PACKAGE_STRIP = "1"

PACKAGE_ARCH = "${MACHINE_ARCH}"

EXTRA_OEMAKE += "CONFIG_ARCH_MSM=y"
EXTRA_OEMAKE += "TARGET_SUPPORT=${@bb.utils.contains('BASEMACHINE', 'sa81x5', 'sa8155', '${BASEMACHINE}', d)}"

RPROVIDES:${PN} += "kernel-module-hsi2s-${KERNEL_VERSION}"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*"
