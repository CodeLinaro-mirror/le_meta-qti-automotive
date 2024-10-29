do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        sed -i '/Description/a\DefaultDependencies=no' ${D}${systemd_unitdir}/system/dbus.socket
        sed -i '/Description/a\Conflicts=shutdown.target' ${D}${systemd_unitdir}/system/dbus.socket
        sed -i '/Description/a\Before=shutdown.target' ${D}${systemd_unitdir}/system/dbus.socket

        sed -i '/Requires/a\DefaultDependencies=no' ${D}${systemd_unitdir}/system/dbus.service
        sed -i '/Requires/a\Conflicts=shutdown.target' ${D}${systemd_unitdir}/system/dbus.service
        sed -i '/Requires/a\Before=shutdown.target' ${D}${systemd_unitdir}/system/dbus.service
    fi
}
