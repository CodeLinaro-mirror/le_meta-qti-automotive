SUMMARY = "Display drivers"
DESCRIPTION = "Build display drivers to kernel module"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "display-devicetree securemsmdlkm"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/display-drivers/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/display-drivers;;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/display-drivers"

TECHPACK_MODULE_OUT = "${WORKDIR}/display-drivers"
TECHPACK_MODULES = "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', \
                    'msm-hyp/msm_hyp.ko msm-cfg/msm_cfg.ko', 'msm/msm_drm.ko', d)}"
TECHPACK_HEADERS = "${S}/include/uapi"
HDCP_QSEECOM_PATCH = "${STAGING_INCDIR}/hdcp_qseecom"
TECHPACK_MAKE_ARGS = "KBUILD_EXTRA_SYMBOLS=${HDCP_QSEECOM_PATCH}/Module.symvers"

inherit qti-techpack

do_install:append:sa81x5(){
    install -m 0644 ${S}/config/display_augen3_load.conf -D ${D}${sysconfdir}/modules-load.d/display_load.conf
}

RPROVIDES:${PN} += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', \
                    'kernel-module-msm-hyp-${KERNEL_VERSION} kernel-module-msm-cfg-${KERNEL_VERSION}', \
                    'kernel-module-msm-drm-${KERNEL_VERSION}', d)}"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
FILES:${PN} += "${sysconfdir}/*"
