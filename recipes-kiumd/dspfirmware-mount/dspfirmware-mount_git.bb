SUMMARY = "Mount dsp and firmware patitions"
DESCRIPTION = "Create dsp and firmware mount targets and install relevant cfg files"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=b796c0007db682166a1721da80267bb2"

SRC_URI = "\
    ${PATH_TO_REPO}/vendor/qcom/opensource/kiumd/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/kiumd;usehead=1 \
    file://mnt_fs.conf \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/kiumd/dspfirmware-mount"
inherit systemd

SYSTEMD_SERVICE:${PN}:sa8797 = "firmware-qcom-sa8797p.automount firmware-qcom-sa8797p.mount"

do_compile[noexec] = "1"

do_install:append() {
    install -d -p ${D}${systemd_unitdir}/system/multi-user.target.wants/

    install -d -p ${D}/vendor/dsp

    install -m 0755 ${WORKDIR}/mnt_fs.conf -D ${D}${libdir}/modules-load.d/mnt_fs.conf
    install -m 0777 ${S}/vendor-dsp.mount -D ${D}${systemd_unitdir}/system/vendor-dsp.mount
    install -m 0777 ${S}/vendor-dsp.automount -D ${D}${systemd_unitdir}/system/vendor-dsp.automount

    if ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'true', 'false', d)}; then
        sed -i '/^Options=/s/defaults/&,context=system_u:object_r:dsp_file_t:s0/' ${D}${systemd_unitdir}/system/vendor-dsp.mount
    fi

    ln -sf ${systemd_unitdir}/system/vendor-dsp.mount \
        ${D}${systemd_unitdir}/system/multi-user.target.wants/vendor-dsp.mount
    ln -sf ${systemd_unitdir}/system/vendor-dsp.automount \
        ${D}${systemd_unitdir}/system/multi-user.target.wants/vendor-dsp.automount

    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-vmm', 'true', 'false', d)}; then
        install -d -p ${D}/firmware/vm/boot
        install -d -p ${D}/firmware/lvgvm/boot

        install -m 0777 ${S}/firmware-vm-boot.automount ${D}${systemd_unitdir}/system/firmware-vm-boot.automount
        install -m 0777 ${S}/firmware-lvgvm-boot.automount ${D}${systemd_unitdir}/system/firmware-lvgvm-boot.automount
        install -m 0777 ${S}/firmware-vm-boot.mount ${D}${systemd_unitdir}/system/firmware-vm-boot.mount
        install -m 0777 ${S}/firmware-lvgvm-boot.mount ${D}${systemd_unitdir}/system/firmware-lvgvm-boot.mount

        if ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'true', 'false', d)}; then
            sed -i '/^Options=/s/defaults/&,context=system_u:object_r:qcrosvm_boot_t:s0/' ${D}${systemd_unitdir}/system/firmware-vm-boot.mount
            sed -i '/^Options=/s/defaults/&,context=system_u:object_r:qcrosvm_boot_t:s0/' ${D}${systemd_unitdir}/system/firmware-lvgvm-boot.mount
        fi

        ln -sf ${systemd_unitdir}/system/firmware-vm-boot.automount \
            ${D}${systemd_unitdir}/system/multi-user.target.wants/firmware-vm-boot.automount
        ln -sf ${systemd_unitdir}/system/firmware-lvgvm-boot.automount \
            ${D}${systemd_unitdir}/system/multi-user.target.wants/firmware-lvgvm-boot.automount
        ln -sf ${systemd_unitdir}/system/firmware-vm-boot.mount \
            ${D}${systemd_unitdir}/system/multi-user.target.wants/firmware-vm-boot.mount
        ln -sf ${systemd_unitdir}/system/firmware-lvgvm-boot.mount \
            ${D}${systemd_unitdir}/system/multi-user.target.wants/firmware-lvgvm-boot.mount
    fi
    if [ -f ${S}/99-persist-storage-ab.rules ]; then
        install -m 0644 ${S}/99-persist-storage-ab.rules -D ${D}${sysconfdir}/udev/rules.d/99-persist-storage-ab.rules
    fi
}

do_install:append:sa8775:sa7255() {
    install -d -p ${D}/firmware/qcom/sa8775p

    install -m 0777 ${S}/firmware-qcom-sa8775p.mount -D ${D}${systemd_unitdir}/system/firmware-qcom-sa8775p.mount
    install -m 0777 ${S}/firmware-qcom-sa8775p.mount -D ${D}${systemd_unitdir}/system/firmware-qcom-sa8775p.automount

    ln -sf ${systemd_unitdir}/system/firmware-qcom-sa8775p.mount \
        ${D}${systemd_unitdir}/system/multi-user.target.wants/firmware-qcom-sa8775p.mount

    ln -sf ${systemd_unitdir}/system/firmware-qcom-sa8775p.mount \
        ${D}${systemd_unitdir}/system/multi-user.target.wants/firmware-qcom-sa8775p.automount
    install -d ${D}${sysconfdir}/sysconfig/
    install -m 0777 ${S}/lpass_cfg ${D}${sysconfdir}/sysconfig/lpass_cfg
    install -m 0777 ${S}/cdsp0_cfg ${D}${sysconfdir}/sysconfig/cdsp0_cfg
    install -m 0777 ${S}/cdsp1_cfg ${D}${sysconfdir}/sysconfig/cdsp1_cfg
    install -m 0777 ${S}/gpdsp0_cfg ${D}${sysconfdir}/sysconfig/gpdsp0_cfg
    install -m 0777 ${S}/gpdsp1_cfg ${D}${sysconfdir}/sysconfig/gpdsp1_cfg
}

do_install:append:sa8797() {
    install -d -p ${D}${systemd_unitdir}/system/multi-user.target.wants/
    install -d -p ${D}/firmware/qcom/sa8797p

    install -m 0777 ${S}/firmware-qcom-sa8797p.mount -D ${D}${systemd_unitdir}/system/firmware-qcom-sa8797p.mount
    install -m 0777 ${S}/firmware-qcom-sa8797p.automount -D ${D}${systemd_unitdir}/system/firmware-qcom-sa8797p.automount

    install -d ${D}${sysconfdir}/sysconfig/
    install -m 0777 ${S}/sa8797_lpass_cfg ${D}${sysconfdir}/sysconfig/sa8797_lpass_cfg
    install -m 0777 ${S}/sa8797_cdsp0_cfg ${D}${sysconfdir}/sysconfig/sa8797_cdsp0_cfg
    install -m 0777 ${S}/sa8797_cdsp1_cfg ${D}${sysconfdir}/sysconfig/sa8797_cdsp1_cfg
    install -m 0777 ${S}/sa8797_gpdsp0_cfg ${D}${sysconfdir}/sysconfig/sa8797_gpdsp0_cfg
    install -m 0777 ${S}/sa8797_gpdsp1_cfg ${D}${sysconfdir}/sysconfig/sa8797_gpdsp1_cfg
}

FILES:${PN}:append:sa8797 = " \
     ${systemd_unitdir}/system/firmware-qcom-sa8797p.mount \
     ${systemd_unitdir}/system/firmware-qcom-sa8797p.automount \
"

FILES:${PN} += "${systemd_unitdir}/*"
FILES:${PN} += "${sysconfdir}/*"
FILES:${PN} += "${libdir}/modules-load.d/*"
FILES:${PN} += "/firmware/*"
FILES:${PN} += "/vendor/*"
