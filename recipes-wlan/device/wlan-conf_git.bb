SUMMARY = "Device Config File for WLAN function validation"
DESCRIPTION = "Device specific example config for WLAN function test. \
               For example, it provide one example hostapd.conf for application \
               to start one Access Point to run on specific channel and set with \
               specific ssid and password. Meanwhile provide one example \
               wpa_supplicant.conf for wpa_supplicant to setup and set the ssid and \
               password to connect to the external AP. The wlan_mac*.bin provide the \
               example about how to set wlan mac address for wireless interface. \
               And the WCNSS_qcom_cfg_qca*.ini file provide the example that \
               WLAN host driver needed while loading, which can disable or enable some \
               WLAN feature.\
               "
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "${PATH_TO_REPO}/device/qcom/wlan/.git;protocol=${PROTO};destsuffix=device/qcom/wlan;usehead=1"
SRCREV = "${AUTOREV}"
PR = "r0"

S = "${WORKDIR}/device"

do_install(){
    install -d ${D}${sysconfdir}/misc/wifi
    install -m 0644 ${S}/qcom/wlan/msm_auto/*.conf ${D}${sysconfdir}/misc/wifi
    install -m 0644 ${S}/qcom/wlan/msm_auto/vendor_cmd.xml ${D}${sysconfdir}/misc/wifi
    install -d ${D}${bindir}
    install -m 0755 ${S}/qcom/wlan/msm_auto/*.sh ${D}${bindir}
}
