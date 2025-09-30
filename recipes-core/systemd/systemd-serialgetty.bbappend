do_install:append() {
        sed -i -e '/SendSIGHUP=yes/a OOMScoreAdjust=0' ${D}${systemd_system_unitdir}/serial-getty@.service
}
