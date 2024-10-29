SUMMARY = "Configure network for GVM"
DESCRIPTION = "Install the systemd service gvm_net_config.service to setup the network bridge for gvm."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=b796c0007db682166a1721da80267bb2"

SYSTEMD_SERVICE:${PN} = "gvm_net_config.service"

SRC_URI = "\
    ${PATH_TO_REPO}/vendor/qcom/opensource/kiumd/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/kiumd;usehead=1 \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/kiumd/gvm_net_config"

inherit systemd

do_compile[noexec] = "1"

do_install:append() {
    install -d -p ${D}/usr/local/bin
    install -m 0755 ${S}/gvm_net_config.sh -D ${D}/usr/local/bin
    sed -i 's/eth1/eth0/g' ${D}/usr/local/bin/gvm_net_config.sh
    install -m 0644 ${S}/gvm_net_config.service -D ${D}${systemd_system_unitdir}/gvm_net_config.service
}

FILES:${PN} += "/usr/local/bin/*"
