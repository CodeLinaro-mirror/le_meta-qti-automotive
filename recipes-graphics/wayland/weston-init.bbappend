FILESEXTRAPATHS:append := " :${THISDIR}/weston/"
SRC_URI = "file://weston.service_caf \
           file://weston.service_caf_10 \
           file://weston_early.service_caf \
           file://weston_early.service_caf_10 \
           file://weston.ini_caf \
           file://weston-autologin \
           file://msm-display-node.rules \
           file://weston.socket \
"
SYSTEMD_SERVICE:${PN} = "weston.service"
SYSTEMD_AUTO_ENABLE = "enable"

REQUIRED_DISTRO_FEATURES:remove = "opengl"

do_install() {
    # Install systemd unit files
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -m 644 -p -D ${WORKDIR}/weston.service_caf_10 ${D}${systemd_system_unitdir}/weston.service
        install -m 644 -p -D ${WORKDIR}/weston.socket ${D}${systemd_system_unitdir}/weston.socket
        if ${@bb.utils.contains('DISTRO_FEATURES', 'early_init', 'true', 'false', d)}; then
            install -m 644 -p -D ${WORKDIR}/weston_early.service_caf_10 ${D}${systemd_system_unitdir}/weston.service
        fi
    fi
    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'true', 'false', d)}; then
        sed -i 's/dev-dri-card0.device/openwfd_server_@0.service kgsl.service/g' ${D}${systemd_system_unitdir}/weston.service
        sed -i '/PAMName/d' ${D}${systemd_system_unitdir}/weston.service
        sed -i '/TTYPath/d' ${D}${systemd_system_unitdir}/weston.service
        sed -i 's/weston --idle-time=0/weston --tty=2 --idle-time=0/' ${D}${systemd_system_unitdir}/weston.service
        sed -i '/Environment/a\ExecStartPre=\/bin\/chmod 700 \/run\/early' ${D}${systemd_system_unitdir}/weston.service
        sed -i '/Environment/a\ExecStartPre=\/bin\/mkdir -p \/run\/early' ${D}${systemd_system_unitdir}/weston.service
        sed -i '/Environment/a\ExecStartPre=\/bin\/chmod 700 \/run\/user\/0' ${D}${systemd_system_unitdir}/weston.service
        sed -i '/Environment/a\ExecStartPre=\/bin\/chmod 700 \/run\/user' ${D}${systemd_system_unitdir}/weston.service
        sed -i '/Environment/a\ExecStartPre=\/bin\/mkdir -p \/run\/user\/0' ${D}${systemd_system_unitdir}/weston.service
    fi
    if [ "${@bb.utils.filter('DISTRO_FEATURES', 'pam', d)}" ]; then
        install -D -p -m0644 ${WORKDIR}/weston-autologin ${D}${sysconfdir}/pam.d/weston-autologin
    fi

    install -D -p -m0644 ${WORKDIR}/weston.ini_caf ${D}${sysconfdir}/xdg/weston/weston.ini
    # Install reuqire-input=false in weston.ini
    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'true', 'false', d)}; then
        sed -i -e '/\[core\]/a require-input=false' ${D}${sysconfdir}/xdg/weston/weston.ini
    fi
    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'true', 'false', d)}; then
        sed -i -e '/\[core\]/a require-input=false' ${D}${sysconfdir}/xdg/weston/weston.ini
    fi

    # Install display udev rule
    install -d ${D}${sysconfdir}/udev/rules.d/
    install -m 0644 ${WORKDIR}/msm-display-node.rules ${D}${sysconfdir}/udev/rules.d/msm-display-node.rules
}

do_install:append:monaco() {
    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'true', 'false', d)}; then
        sed -i 's/openwfd_server_@0.service kgsl.service/openwfd_server_@0.service/g' ${D}${systemd_system_unitdir}/weston.service
        sed -i 's/rc.pvr.service openwfd_server_@0.service/rc.pvr.service openwfd_server_@0.service multi-user.target/g' ${D}${systemd_system_unitdir}/weston.service
    fi
}

do_install:append:sa8775() {
    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'true', 'false', d)}; then
        sed -i 's/systemd-notify.so/systemd-notify.so,compositor-pm-ds.so/g' ${D}${systemd_system_unitdir}/weston.service
    fi
}
