SUMMARY = "Display drivers"
DESCRIPTION = "Build display drivers to kernel module"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "display-devicetree securemsmdlkm"
DEPENDS:remove:gvm-gen5 = "display-devicetree"
DEPENDS:remove:gvm-gen4-5 = "display-devicetree"
DEPENDS:append:gvm-gen5 = " msm-ext-display"

SRC_URI = "${PATH_TO_REPO}/${TARGET_DIR}vendor/qcom/opensource/display-drivers/.git;protocol=${PROTO};destsuffix=${TARGET_DIR}vendor/qcom/opensource/display-drivers;;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/${TARGET_DIR}vendor/qcom/opensource/display-drivers"

EXT_MODULE = "${TARGET_DIR}vendor/qcom/opensource/display-drivers"

TECHPACK_MODULE_OUT = "${WORKDIR}/display-drivers"
TECHPACK_MODULES = "${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '6.12', bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', \
                    'msm_hyp.ko msm_cfg.ko', 'msm_drm.ko', d),bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor',\
                    'msm-hyp/msm_hyp.ko msm-cfg/msm_cfg.ko', 'msm/msm_drm.ko', d), d)}"
# hw virtualization
TECHPACK_MODULES:gvm-gen5 = "msm_drm.ko"
TECHPACK_HEADERS = "${S}/include/uapi"
HDCP_QSEECOM_PATCH = "${STAGING_INCDIR}/hdcp_qseecom"
TECHPACK_MAKE_ARGS = "KBUILD_EXTRA_SYMBOLS=${HDCP_QSEECOM_PATCH}/Module.symvers"
# gvm-gen5: module.bbclass anonymous function already sets KBUILD_EXTRA_SYMBOLS
# to kernel-module-soc-modules/Module.symvers (needed for qtee_shmbridge_*,
# qcom_scm_*, etc.).  Passing a second KBUILD_EXTRA_SYMBOLS= on the command
# line (via TECHPACK_MAKE_ARGS) would override and lose the soc-modules entry.
# Instead, clear TECHPACK_MAKE_ARGS and append hdcp_qseecom to the BitBake
# variable so both symvers files end up in the single quoted argument that
# module_do_compile passes to make.
TECHPACK_MAKE_ARGS:gvm-gen5 = ""
KBUILD_EXTRA_SYMBOLS:append:gvm-gen5 = " ${HDCP_QSEECOM_PATCH}/Module.symvers"
TECHPACK_MAKE_ARGS:gvm-gen4-5 = "DISPLAY_ROOT=${S}"

inherit qti-techpack

do_compile:prepend:gvm-gen5(){
    export GEN5_LVGVM=y
    export ROOTDIR="${WORKDIR}"
    export MM_DRIVERS_INC="${STAGING_INCDIR}"
}

do_install:append:sa81x5(){
    install -m 0644 ${S}/config/display_augen3_load.conf -D ${D}${sysconfdir}/modules-load.d/display_load.conf
}

RDEPENDS:${PN}:gvm-gen5 += "msm-ext-display"

RPROVIDES:${PN} += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', \
                    'kernel-module-msm-hyp-${KERNEL_VERSION} kernel-module-msm-cfg-${KERNEL_VERSION}', \
                    'kernel-module-msm-drm-${KERNEL_VERSION}', d)}"
RPROVIDES:${PN}:gvm-gen5 += "kernel-module-msm-drm-${KERNEL_VERSION}"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
FILES:${PN} += "${sysconfdir}/*"
