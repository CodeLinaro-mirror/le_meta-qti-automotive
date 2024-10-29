require qcacld32-ll.inc

SUMMARY = "Qualcomm Atheros WLAN Driver"
DESCRIPTION = "Qualcomm Atheros WLAN CLD3.0 low latency driver for the first WLAN chip.\
               It is a kernel extra module, which loaded by init_qti_wlan_auto.service \
               once the system bootup. And this WLAN host driver module name is qca6696.ko,\
               it create two interface by defaults, one is wlan0 and the other is wlan1. \
               Application can use the wireless interfaces as STA or AP mode in need. \
               Usually, it bind to pcie0 slot by default if it loaded first. \"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=f3b90e78ea0cffb20bf5cca7947a896d"

PR = "r8"
SRC_URI = "${PATH_TO_REPO}/wlan/qcacld-3.0/.git;protocol=${PROTO};name=qcacld;destsuffix=wlan/qcacld-3.0;usehead=1 \
           ${PATH_TO_REPO}/wlan/qca-wifi-host-cmn/.git;protocol=${PROTO};name=qca-wifi-host-cmn;destsuffix=wlan/qca-wifi-host-cmn;usehead=1 \
           ${PATH_TO_REPO}/wlan/fw-api/.git;protocol=${PROTO};name=fw-api;destsuffix=wlan/fw-api/;usehead=1 \
           ${PATH_TO_REPO}/device/qcom/wlan/.git;protocol=${PROTO};name=wlan;destsuffix=device/qcom/wlan;usehead=1 \
           "
SRCREV_qcacld = "${AUTOREV}"
SRCREV_qca-wifi-host-cmn = "${AUTOREV}"
SRCREV_fw-api = "${AUTOREV}"
SRCREV_wlan = "${AUTOREV}"
SRCREV_FORMAT = "qcacld_cmn_fw_msm"

_MODNAME = "qca6696"
_WLAN_CTRL_NAME = "wlan"
FW_PATH_NAME = "qca6390"
FIRMWARE_PATH = "${D}${nonarch_base_libdir}/firmware/wlan/qca_cld/${_MODNAME}"

S1 = "${WORKDIR}/wlan/qca-wifi-host-cmn"
S = "${WORKDIR}/wlan/qcacld-3.0"

# Explicitly disable HL to enable LL as current WLAN driver is not having
# simultaneous support of HL and LL.
EXTRA_OEMAKE:append = " \
                       LINUX_BUILD_TOP=${_LINUX_BUILD_TOP} \
                       CONFIG_CLD_HL_SDIO_CORE=n \
                       CONFIG_CNSS_SDIO=n \
                       CONFIG_QCA_CLD_WLAN_PROFILE=qca6390 \
                       DYNAMIC_SINGLE_CHIP=${_MODNAME} \
                       MULTI_IF_NAME=cnss0 \
                       MODNAME=${_MODNAME} \
                       WLAN_CTRL_NAME=${_WLAN_CTRL_NAME} \
                       "

_WLAN_CFG_OVERRIDE = "\
                      CONFIG_WLAN_DISABLE_EXPORT_SYMBOL=y \
                      CONFIG_REO_QDESC_HISTORY=y \
                      CONFIG_REO_DESC_DEFER_FREE=y \
                      CONFIG_HIF_DEBUG=y \
                      CONFIG_HIF_CE_DEBUG_DATA_BUF=y \
                      CONFIG_BUS_AUTO_SUSPEND=n \
                      CONFIG_DUP_RX_DESC_WAR=y \
                      CONFIG_WLAN_FEATURE_DP_EVENT_HISTORY=y \
                      CONFIG_DP_TX_TRACKING=y \
                      "

_WLAN_CFG_OVERRIDE_GVM = "\
                        CONFIG_WLAN_BOOTUP_MARKER=y \
                        CONFIG_WLAN_PLACEMARKER_PREFIX=108 \
                        CONFIG_FEATURE_GPIO_CFG=y \
                        CONFIG_CNSS_GENL=n \
                        CONFIG_MULTI_IF_LOG=y \
                        CONFIG_FEATURE_WLAN_CH_AVOID_EXT=y \
                        CONFIG_QCOM_TDLS=n \
                        CONFIG_CFG_MAX_STA_VDEVS=4 \
                        CONFIG_CFG_BMISS_OFFLOAD_MAX_VDEV=4 \
                        "

_WLAN_CFG_OVERRIDE_PRODUCT = "\
                        CONFIG_WLAN_DISABLE_EXPORT_SYMBOL=y \
                        CONFIG_WLAN_OPEN_P2P_INTERFACE=n \
                        CONFIG_SUPPORT_P2P_BY_ONE_INTF_WLAN=y \
                        CONFIG_FEATURE_GPIO_CFG=y \
                        CONFIG_WLAN_MAC_ADDR_UPDATE_DISABLE=y \
                        CONFIG_FEATURE_WLAN_CH_AVOID_EXT=y \
                        CONFIG_QCOM_TDLS=n \
                        CONFIG_CFG_MAX_STA_VDEVS=4 \
                        CONFIG_CFG_BMISS_OFFLOAD_MAX_VDEV=4 \
                        CONFIG_REO_DESC_DEFER_FREE=y \
                        CONFIG_FEATURE_MEMDUMP_ENABLE=n \
                        WLAN_OPEN_SOURCE=n \
                        CONFIG_REMOVE_PKT_LOG=y \
                        CONFIG_DEBUG_FS=n \
                        CONFIG_WLAN_SYSFS=n \
                        CONFIG_WLAN_SYSFS_MEM_STATS=n \
                        CONFIG_SLUB_DEBUG=n \
                        CONFIG_PERF_DEBUG=n \
                        "

_WLAN_CFG_NO_SEPARATE_P2P = "CONFIG_WLAN_OPEN_P2P_INTERFACE=n CONFIG_SUPPORT_P2P_BY_ONE_INTF_WLAN=y"

_WLAN_CFG_SEPARATE_P2P = "CONFIG_WLAN_OPEN_P2P_INTERFACE=y CONFIG_SUPPORT_P2P_BY_ONE_INTF_WLAN=n"

_WLAN_CFG_OVERRIDE:append:qtiquingvm = "${_WLAN_CFG_OVERRIDE_GVM} ${_WLAN_CFG_NO_SEPARATE_P2P}"

_WLAN_CFG_OVERRIDE:append:qtiquingvm8295 = "${_WLAN_CFG_OVERRIDE_GVM} ${_WLAN_CFG_NO_SEPARATE_P2P}"

_WLAN_CFG_OVERRIDE:append:quin-gvm-gen4 = "${_WLAN_CFG_OVERRIDE_GVM} ${_WLAN_CFG_NO_SEPARATE_P2P}"

_WLAN_CFG_OVERRIDE:append:quin-gvm-gen4-2 = "${_WLAN_CFG_OVERRIDE_GVM} ${_WLAN_CFG_NO_SEPARATE_P2P}"

_WLAN_CFG_OVERRIDE:append:sa8295adp-2 = "${_WLAN_CFG_OVERRIDE_GVM} ${_WLAN_CFG_SEPARATE_P2P}"

EXTRA_OEMAKE += "WLAN_CFG_OVERRIDE=${_WLAN_CFG_OVERRIDE}"

do_install() {
    module_do_install

    install -d ${FIRMWARE_PATH}
    install -d ${D}${includedir}/qcacld/
    install -m 0644 ${S1}/utils/nlink/inc/wlan_nlink_common.h ${D}${includedir}/qcacld/

    #copying wlan.ko to STAGING_DIR_TARGET
    WLAN_KO=${@oe.utils.conditional('PERF_BUILD', '1', '${STAGING_DIR_TARGET}-perf', '${STAGING_DIR_TARGET}', d)}
    install -d ${WLAN_KO}/wlan
    install -m 0644 ${S}/${_MODNAME}.ko ${WLAN_KO}/wlan/

    install -D -m 0644 ${WORKDIR}/device/qcom/wlan/msm_auto/WCNSS_qcom_cfg_qca6390.ini ${FIRMWARE_PATH}/WCNSS_qcom_cfg.ini
    install -D -m 0644 ${WORKDIR}/device/qcom/wlan/msm_auto/wlan_mac_hst_1.bin ${FIRMWARE_PATH}/wlan_mac.bin

    install -d ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/
    ln -sf /firmware/image/${FW_PATH_NAME}/amss.bin ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/
    ln -sf /firmware/image/${FW_PATH_NAME}/amss20.bin ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/
    ln -sf /firmware/image/${FW_PATH_NAME}/bdwlan02.e01 ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/
    ln -sf /firmware/image/${FW_PATH_NAME}/bdwlan02.e02 ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/
    ln -sf /firmware/image/${FW_PATH_NAME}/bdwlan.elf ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/
    ln -sf /firmware/image/${FW_PATH_NAME}/m3.bin ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/
}
