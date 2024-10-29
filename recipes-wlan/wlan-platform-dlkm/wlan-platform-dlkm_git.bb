SUMMARY = "Wlan platform drivers"
DESCRIPTION = "Build wlan platform drivers to kernel module"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DEPENDS = "wlan-devicetree"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/wlan/platform/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/wlan/platform;;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/wlan/platform"

TECHPACK_MODULE_OUT = "${WORKDIR}/wlan-platform-dlkm"
TECHPACK_MODULES = "cnss2/cnss2.ko"
TECHPACK_MODULES:append = " cnss_utils/cnss_utils.ko"
TECHPACK_MODULES:append = " cnss_utils/wlan_firmware_service.ko"
TECHPACK_MODULES:append = " cnss_utils/cnss_plat_ipc_qmi_svc.ko"
TECHPACK_MODULES:append = " cnss_genl/cnss_nl.ko"

inherit qti-techpack

WLAN_PLATFORM_CFG = "\
                     USE_EXTERNAL_CONFIGS=y \
                     CONFIG_CNSS_OUT_OF_TREE=y \
                     CONFIG_CNSS2=m \
                     CONFIG_AUTO_PROJECT=y \
                     CONFIG_CNSS2_QMI=y \
                     CONFIG_CNSS2_DEBUG=y \
                     CONFIG_CNSS_QMI_SVC=m \
                     CONFIG_CNSS_PLAT_IPC_QMI_SVC=m \
                     CONFIG_CNSS_GENL=m \
                     CONFIG_CNSS_UTILS=m \
                     CONFIG_CNSS2_CONDITIONAL_POWEROFF=y \
                     CONFIG_CNSS_REQ_FW_DIRECT=y \
                     CONFIG_CNSS2_ENUM_WITH_LOW_SPEED=y \
                     "

WLAN_PLATFORM_CFG_PROD = "\
                     USE_EXTERNAL_CONFIGS=y \
                     CONFIG_CNSS_OUT_OF_TREE=y \
                     CONFIG_CNSS2=m \
                     CONFIG_AUTO_PROJECT=y \
                     CONFIG_CNSS2_QMI=y \
                     CONFIG_CNSS_QMI_SVC=m \
                     CONFIG_CNSS_PLAT_IPC_QMI_SVC=m \
                     CONFIG_CNSS_GENL=m \
                     CONFIG_CNSS_UTILS=m \
                     CONFIG_CNSS2_CONDITIONAL_POWEROFF=y \
                     CONFIG_CNSS_SUPPORT_DUAL_DEV=y \
                     CONFIG_CNSS_REQ_FW_DIRECT=y \
                     "

EXTRA_OEMAKE:append = " ${WLAN_PLATFORM_CFG}"
TECHPACK_MAKE_ARGS = "${EXTRA_OEMAKE} QTI_TECHPACK=true"

do_install:append(){
    dlkmdir=${D}${includedir}/wlan-platform
    install -d ${dlkmdir}
    install -d ${dlkmdir}/inc
    install -m 0644 ${S}/Module.symvers ${dlkmdir}/
    install -m 0644 ${S}/inc/* ${dlkmdir}/inc/
}

RPROVIDES:${PN} += "kernel-module-cnss-nl-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-cnss-plat-ipc-qmi-svc-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-wlan-firmware-service-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-cnss-utils-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-cnss2-${KERNEL_VERSION}"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
FILES:${PN} += "${includedir}/wlan-platform/*"
