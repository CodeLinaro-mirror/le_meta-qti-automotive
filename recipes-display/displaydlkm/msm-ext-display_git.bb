SUMMARY = "msm-ext-display drivers"
DESCRIPTION = "Build display drivers msm_ext_display.ko"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "${PATH_TO_REPO}/${TARGET_DIR}vendor/qcom/opensource/mm-drivers/.git;protocol=${PROTO};destsuffix=${TARGET_DIR}vendor/qcom/opensource/mm-drivers;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/${TARGET_DIR}vendor/qcom/opensource/mm-drivers/msm_ext_display"

EXT_MODULE = "${TARGET_DIR}vendor/qcom/opensource/mm-drivers/msm_ext_display"
TECHPACK_MODULE_OUT = "${WORKDIR}/mm-drivers/msm_ext_display"
TECHPACK_MODULES = "msm_ext_display.ko"
TECHPACK_HEADERS = "${WORKDIR}/${TARGET_DIR}vendor/qcom/opensource/mm-drivers/msm_ext_display/include/uapi"

inherit qti-techpack

do_install:append:gvm-gen5(){
    install -d ${D}${includedir}/hw_fence/include
    install -m 644 ${S}/../hw_fence/include/*.h ${D}${includedir}/hw_fence/include/
}

RPROVIDES:${PN} += "kernel-module-msm-ext-display-${KERNEL_VERSION}"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
FILES:${PN} += "${sysconfdir}/*"
