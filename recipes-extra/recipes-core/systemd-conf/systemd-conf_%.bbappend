FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " file://90-powerkey-conf.conf"

do_install:append() {
    # Use powerkey to do suspend on qti platform.
    install -d ${D}${systemd_unitdir} ${D}${systemd_unitdir}/logind.conf.d
    install -m 0644 ${WORKDIR}/90-powerkey-conf.conf ${D}${systemd_unitdir}/logind.conf.d

    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'false', 'true', d)}; then
        echo -e "[Login]\nHandlePowerKey=ignore\nHandleSuspendKey=ignore" > ${D}${systemd_unitdir}/logind.conf.d/90-powerkey-conf.conf
    fi

    #Override default setting and add RuntimeWatchdogSec & RebootWatchdogSec support for ark/qclinux kernel
    if ${@bb.utils.contains_any('PREFERRED_PROVIDER_virtual/kernel', 'linux-ark linux-qcom-custom linux-qcom-custom-rt', 'true', 'false', d)}; then
        echo "RuntimeWatchdogSec=10" >> ${D}${systemd_unitdir}/system.conf.d/00-${PN}.conf
        echo "RebootWatchdogSec=32" >> ${D}${systemd_unitdir}/system.conf.d/00-${PN}.conf
    fi
}
