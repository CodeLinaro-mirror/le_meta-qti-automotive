SUMMARY = "vidc driver"
DESCRIPTION = "Recipe to generate the vidc driver module. The driver configures the vidc interfaces in the video subsystem."
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "video-devicetree"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/video-driver/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/video-driver;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/video-driver"

TECHPACK_MODULE_OUT = "${WORKDIR}/vendor/qcom/opensource/video-driver-out"
TECHPACK_MODULES = "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '', 'msm-vidc.ko',d)}"
TECHPACK_HEADERS = "${S}/include/uapi"

inherit qti-techpack

RPROVIDES:${PN} += "kernel-module-msm-vidc-${KERNEL_VERSION}"
FILES:${PN} += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '', '${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*', d)}"
ALLOW_EMPTY:${PN} = "1"
