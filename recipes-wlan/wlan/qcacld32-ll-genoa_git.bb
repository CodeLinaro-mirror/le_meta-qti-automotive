require qcacld32-ll.inc

SUMMARY = "Qualcomm Atheros WLAN Driver"
DESCRIPTION = "Qualcomm Atheros WLAN CLD3.0 low latency driver for Genoa WLAN chip.\
               It is a kernel extra module, which loaded by init_qti_wlan_auto.service \
               once the system bootup. And this WLAN host driver module name is qca6696.ko,\
               it create two interface by defaults, one is wlan0 and the other is p2p0. \
               Application can use the wireless interfaces as STA/AP/P2P mode in need. \"
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

_MODNAME = "qca6595"
_WLAN_CTRL_NAME = "wlan"
FW_PATH_NAME = "qcn7605"
FIRMWARE_PATH = "${D}${nonarch_base_libdir}/firmware/wlan/qca_cld/${_MODNAME}"

S1 = "${WORKDIR}/wlan/qca-wifi-host-cmn"
S = "${WORKDIR}/wlan/qcacld-3.0"

# Explicitly disable HL to enable LL as current WLAN driver is not having
# simultaneous support of HL and LL.
EXTRA_OEMAKE:append = " \
                       LINUX_BUILD_TOP=${_LINUX_BUILD_TOP} \
                       CONFIG_CLD_HL_SDIO_CORE=n \
                       CONFIG_CNSS_SDIO=n \
                       CONFIG_QCA_CLD_WLAN_PROFILE=genoa.pci.debug \
                       DYNAMIC_SINGLE_CHIP=${_MODNAME} \
                       MODNAME=${_MODNAME} \
                       CONFIG_CNSS_GENL=n \
                       CONFIG_IPA_OFFLOAD=n \
                       WLAN_CTRL_NAME=${_WLAN_CTRL_NAME} \
                       "

_WLAN_CFG_OVERRIDE = "\
                      CONFIG_WLAN_MAX_VDEVS=4 \
                      CONFIG_QCACLD_FEATURE_BTC_CHAIN_MODE=y \
                      CONFIG_FEATURE_COEX=y \
                      CONFIG_QCACLD_FEATURE_COEX_CONFIG=y \
                      CONFIG_WLAN_FEATURE_LINK_LAYER_STATS=y \
                      CONFIG_HIF_DEBUG=y \
                      CONFIG_HIF_CE_DEBUG_DATA_BUF=y \
                      CONFIG_SIR_SAP_MAX_NUM_PEERS=32 \
                      "

_WLAN_CFG_OVERRIDE:append:sa6155 = " CONFIG_IPA_DISABLE_OVERRIDE=y"

EXTRA_OEMAKE:append = " WLAN_CFG_OVERRIDE="${_WLAN_CFG_OVERRIDE}""

do_install() {
    module_do_install

    install -d ${FIRMWARE_PATH}
    install -d ${D}${includedir}/qcacld/
    install -m 0644 ${S1}/utils/nlink/inc/wlan_nlink_common.h ${D}${includedir}/qcacld/

    #copying wlan.ko to STAGING_DIR_TARGET
    WLAN_KO=${@oe.utils.conditional('PERF_BUILD', '1', '${STAGING_DIR_TARGET}-perf', '${STAGING_DIR_TARGET}', d)}
    install -d ${WLAN_KO}/wlan
    install -m 0644 ${S}/${_MODNAME}.ko ${WLAN_KO}/wlan/

    install -D -m 0644 ${WORKDIR}/device/qcom/wlan/msm_auto/WCNSS_qcom_cfg_qcn7605.ini ${FIRMWARE_PATH}/WCNSS_qcom_cfg.ini
    install -D -m 0644 ${WORKDIR}/device/qcom/wlan/msm_auto/wlan_mac.bin ${FIRMWARE_PATH}/wlan_mac.bin

    install -d ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/
    ln -sf /firmware/image/${FW_PATH_NAME}/amss.bin ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/
    #For GNA04.1 boardid = 0xff
    ln -sf /firmware/image/${FW_PATH_NAME}/bdwlan03.b01 ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/
    mv ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/bdwlan03.b01 ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/bdwlan.bin
    #For GNA04.1 boardid = 0x301
    ln -sf /firmware/image/${FW_PATH_NAME}/bdwlan03.b01 ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/
    mv ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/bdwlan03.b01 ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/bdwlan.b0301
    #For GNA04.1 boardid = 0x302
    ln -sf /firmware/image/${FW_PATH_NAME}/bdwlan02.b03 ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/
    mv ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/bdwlan02.b03 ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/bdwlan03.b02
    #For GNA04.1 boardid = 0x203
    ln -sf /firmware/image/${FW_PATH_NAME}/bdwlan02.b03 ${D}${nonarch_base_libdir}/firmware/${FW_PATH_NAME}/
}
