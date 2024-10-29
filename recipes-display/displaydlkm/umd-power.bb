SUMMARY = "UMD Power Driver For Multimedia"
DESCRIPTION = "Build umd power control to kernel module"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "umd-power-devicetree"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/display-drivers/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/display-drivers;;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/display-drivers/umd-mm"

TECHPACK_MODULE_OUT = "${WORKDIR}/display-drivers"
TECHPACK_MODULES = "umd_power.ko"
TECHPACK_HEADERS = "${S}/include/uapi"

inherit qti-techpack

RPROVIDES:${PN} += "kernel-module-umd-power-${KERNEL_VERSION}"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
FILES:${PN} += "${sysconfdir}/*"
