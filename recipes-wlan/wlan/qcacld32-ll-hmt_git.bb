require qcacld32-ll.inc

SUMMARY = "Qualcomm Technologies, Inc. WLAN Driver"
DESCRIPTION = "Qualcomm Technologies, Inc. WLAN CLD3.0 low latency driver for HastingsPrime WLAN chip.\
               It is a kernel extra module, which loaded by init_qti_wlan_auto.service \
               once the system bootup. And this WLAN host driver module name is qca6797.ko,\
               it create two interface by default, one is wlan0 and the other is wlan1. \
               Application can use the wireless interfaces as STA/AP/P2P mode in need. \"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=f3b90e78ea0cffb20bf5cca7947a896d"

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

_MODNAME = "qca6797"
_WLAN_CTRL_NAME = "wlan"
FIRMWARE_PATH = "${D}${nonarch_base_libdir}/firmware/wlan/qca_cld/${_MODNAME}"

S1 = "${WORKDIR}/wlan/qca-wifi-host-cmn"
S = "${WORKDIR}/wlan/qcacld-3.0"

# Explicitly disable HL to enable LL as current WLAN driver is not having
# simultaneous support of HL and LL.
EXTRA_OEMAKE:append = " \
                       CONFIG_CLD_HL_SDIO_CORE=n \
                       CONFIG_CNSS_SDIO=n \
                       CONFIG_QCA_CLD_WLAN_PROFILE=kiwi_v2 \
                       DYNAMIC_SINGLE_CHIP=${_MODNAME} \
                       MODNAME=${_MODNAME} \
                       WLAN_CTRL_NAME=${_WLAN_CTRL_NAME} \
                       "

_WLAN_CFG_OVERRIDE = "\
                        LINUX_BUILD_TOP=${_LINUX_BUILD_TOP} \
                        CONFIG_WLAN_OPEN_P2P_INTERFACE=y \
                        CONFIG_SUPPORT_P2P_BY_ONE_INTF_WLAN=n \
                        CONFIG_WLAN_PLACEMARKER_PREFIX=108 \
                        CONFIG_CNSS_GENL=n \
                        CONFIG_QCOM_TDLS=n \
                        CONFIG_CFG_MAX_STA_VDEVS=4 \
                        CONFIG_CFG_BMISS_OFFLOAD_MAX_VDEV=4 \
                        CONFIG_BAND_6GHZ=y \
                        CONFIG_CONNECTION_ROAMING_CFG=n \
                        CONFIG_DBR_HOLD_LARGE_MEM=n \
                        CONFIG_DP_MULTIPASS_SUPPORT=n \
                        CONFIG_FEATURE_DELAYED_PEER_OBJ_DESTROY=n \
                        "
EXTRA_OEMAKE:append = " WLAN_CFG_OVERRIDE=${_WLAN_CFG_OVERRIDE}"

do_install() {
    module_do_install

    install -d ${FIRMWARE_PATH}
    install -d ${D}${includedir}/qcacld/
    install -m 0644 ${S1}/utils/nlink/inc/wlan_nlink_common.h ${D}${includedir}/qcacld/

    install -D -m 0644 ${WORKDIR}/device/qcom/wlan/msm_auto/WCNSS_qcom_cfg_qca6797.ini ${FIRMWARE_PATH}/WCNSS_qcom_cfg.ini
    install -D -m 0644 ${WORKDIR}/device/qcom/wlan/msm_auto/wlan_mac.bin ${FIRMWARE_PATH}/wlan_mac.bin

}
