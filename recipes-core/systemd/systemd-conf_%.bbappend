FILESEXTRAPATHS:prepend := "${THISDIR}/systemd-conf:"

SRC_URI:append = " file://10-eth-mac-policy.conf"

FILES:${PN} += "${sysconfdir}/systemd/network/99-default.link.d/10-eth-mac-policy.conf"

do_install:append() {
        install -d ${D}${sysconfdir}/systemd/network/99-default.link.d/
        install -m 0644 ${WORKDIR}/10-eth-mac-policy.conf \
                ${D}${sysconfdir}/systemd/network/99-default.link.d/

        rm ${D}${systemd_unitdir}/network/80-wired.network
        # Disable ForwardToSyslog from systemd-conf.bb for better performance
        [ -f ${D}${systemd_unitdir}/journald.conf.d/00-systemd-conf.conf ] && \
                sed -i "/ForwardToSyslog=yes/d" ${D}${systemd_unitdir}/journald.conf.d/00-systemd-conf.conf
        [ -f ${D}${systemd_unitdir}/journald.conf.d/00-systemd-conf.conf ] && \
                echo "Compress=no" >> ${D}${systemd_unitdir}/journald.conf.d/00-systemd-conf.conf
        [ -f ${D}${systemd_unitdir}/journald.conf.d/00-systemd-conf.conf ] && \
                echo "Audit=no" >> ${D}${systemd_unitdir}/journald.conf.d/00-systemd-conf.conf
        [ -f ${D}${systemd_unitdir}/system.conf.d/00-systemd-conf.conf ] && \
                echo "DefaultOOMScoreAdjust=-100" >> ${D}${systemd_unitdir}/system.conf.d/00-systemd-conf.conf
}

