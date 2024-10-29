inherit module qperf qti-kernel-arch-clang

DESCRIPTION = "EMAC Ethernet driver"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://DWC_ETH_QOS_dev.c;\
startline=1;endline=71;md5=62b57cd65ebb8a65e225a5fbfbc26932"

FILES:${PN} += "${sysconfdir}/init.d/emac_dwc_eqos_start_stop_le"
FILES:${PN} += "${sysconfdir}/init.d/setup_avtp_routing_le"
FILES:${PN} += "${systemd_unitdir}/system/emac_dwc_eqos.service"
FILES:${PN} += "${systemd_unitdir}/system/multi-user.target.wants/emac_dwc_eqos.service"
FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*"

do_unpack[deptask] = "do_populate_sysroot"
PR = "r0"

SRC_URI = "${PATH_TO_REPO}/data-kernel/.git;protocol=${PROTO};destsuffix=data-kernel;usehead=1"
SRC_URI:append = " file://emac_dwc_eqos_start_stop_le"
SRC_URI:append = " file://setup_avtp_routing_le"
SRC_URI:append = " file://emac_dwc_eqos.service"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/data-kernel/drivers/emac-dwc-eqos"

EXTRA_OEMAKE += "LBITS=32"
EXTRA_OEMAKE += "DWC_ETH_QOS_ENABLE_ETHTOOL=1"
EXTRA_OEMAKE += "${@bb.utils.contains('MACHINE_FEATURES', 'ipa-offload', 'CONFIG_IPA_OFFLOAD=1', '', d)}"

do_install() {
    module_do_install
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/emac_dwc_eqos_start_stop_le ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/setup_avtp_routing_le ${D}${sysconfdir}/init.d

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d ${D}${systemd_unitdir}/system/
        install -m 0644 ${WORKDIR}/emac_dwc_eqos.service -D ${D}${systemd_unitdir}/system/emac_dwc_eqos.service
        install -d ${D}${systemd_unitdir}/system/multi-user.target.wants/
        # enable the service for multi-user.target
        ln -sf ${systemd_unitdir}/system/emac_dwc_eqos.service \
        ${D}${systemd_unitdir}/system/multi-user.target.wants/emac_dwc_eqos.service
    fi
}

pkg_postinst:${PN} () {
    [ -n "$D" ] && OPT="-r $D" || OPT="-s"
    update-rc.d $OPT -f emac_dwc_eqos_start_stop_le remove
    update-rc.d $OPT emac_dwc_eqos_start_stop_le start 37 S . stop 63 0 1 6 .
    update-rc.d $OPT -f setup_avtp_routing_le remove
    update-rc.d $OPT setup_avtp_routing_le start 91 S . stop 9 0 1 6 .
}

do_module_signing() {
    if [ "${BASEMACHINE}" = "sa81x5" ]; then
    if [ -f  ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.pem ]; then
        bbnote "Signing ${PN} module ${i}"
        for i in $(find ${PKGDEST}/${PN}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/ -name "*.ko"); do
          ${STAGING_KERNEL_BUILDDIR}/scripts/sign-file sha512 ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.pem ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.x509 ${i}
        done
    else
        bbnote "${PN} module is not being signed"
    fi
    elif [ -f ${STAGING_KERNEL_BUILDDIR}/signing_key.priv ]; then
        bbnote "Signing ${PN} module"
        ${STAGING_KERNEL_DIR}/scripts/sign-file sha512 ${STAGING_KERNEL_BUILDDIR}/signing_key.priv \
            ${STAGING_KERNEL_BUILDDIR}/signing_key.x509 \
            ${PKGDEST}/${PROVIDES_NAME}/lib/modules/$${KERNEL_VERSION}/extra/emac_dwc_eqos.ko
    else
        bbnote "${PN} module is not being signed"
    fi
}

addtask module_signing after do_package before do_package_write_ipk

RPROVIDES:${PN} += "${@'kernel-module-emac-dwc-eqos-${KERNEL_VERSION}'.replace('_', '-')}"
# uncomment below line if you are compiling test module for vipertooth
#RPROVIDES:${PN} += "${@'kernel-module-dwc-eth-qos-testmod-${KERNEL_VERSION}'.replace('_', '-')}"
