SUMMARY = "QTI Bluetooth Kernel Module"
DESCRIPTION = "QTI Bluetooth Kernel Module mainly includes btpower driver to\
power on/off QTI Bluetooth chips"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DEPENDS = "bt-devicetree"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/bt-kernel/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/bt-kernel;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/bt-kernel"

TECHPACK_MODULE_OUT = "${WORKDIR}/bt-dlkm"
TECHPACK_MODULES = "pwr/btpower.ko"
TECHPACK_MAKE_ARGS = "CONFIG_MSM_BT_POWER=m"

inherit qti-techpack

RPROVIDES:${PN} += "kernel-module-btpower-${KERNEL_VERSION}"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
