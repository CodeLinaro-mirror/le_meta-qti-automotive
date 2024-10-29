SUMMARY = "Camera drivers"
DESCRIPTION = "Build QCX camera drivers to kernel module"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/qcx-kernel/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/qcx-kernel;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/qcx-kernel"

TECHPACK_MODULE_OUT = "${WORKDIR}/qcx-kernel"
TECHPACK_MODULES = "camera.ko"
TECHPACK_HEADERS = "${S}/include/uapi"

inherit qti-techpack

do_compile:prepend() {
    export ROOT_DIR=""
    export KERNEL_DIR=${STAGING_KERNEL_DIR}
}

RPROVIDES:${PN} += "kernel-module-camera-${KERNEL_VERSION}"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
FILES:${PN} += "${sysconfdir}/*"
