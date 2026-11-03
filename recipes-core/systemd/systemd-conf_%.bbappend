FILESEXTRAPATHS:prepend := "${THISDIR}/systemd-conf:"

SRC_URI:append = " file://10-eth-mac-policy.conf file://00-keep-boot-tmp.conf"

FILES:${PN} += "${sysconfdir}/systemd/network/99-default.link.d/10-eth-mac-policy.conf ${sysconfdir}/tmpfiles.d/00-keep-boot-tmp.conf"

do_install:append() {
        install -d ${D}${sysconfdir}/systemd/network/99-default.link.d/
        install -m 0644 ${WORKDIR}/10-eth-mac-policy.conf \
                ${D}${sysconfdir}/systemd/network/99-default.link.d/

        install -d ${D}${sysconfdir}/tmpfiles.d/
        install -m 0644 ${WORKDIR}/00-keep-boot-tmp.conf ${D}${sysconfdir}/tmpfiles.d/

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

