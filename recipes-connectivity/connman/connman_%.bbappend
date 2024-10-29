# Fix connman resolve failed when enable read-only-rootfs

do_install:append() {
    if ${@bb.utils.contains('IMAGE_FEATURES','read-only-rootfs','true','false',d)}; then
        if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
            sed -i -e '/^L+/d'  ${D}${sysconfdir}/tmpfiles.d/connman_resolvconf.conf
        fi
    fi
    # Disable connman for lxc container
    sed -i -e '/^RequiresMountsFor.*$/a ConditionVirtualization=!container' ${D}${systemd_system_unitdir}/connman.service
}
