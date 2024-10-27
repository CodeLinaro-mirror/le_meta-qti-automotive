do_install:append() {
        rm ${D}${systemd_unitdir}/network/80-wired.network
}

