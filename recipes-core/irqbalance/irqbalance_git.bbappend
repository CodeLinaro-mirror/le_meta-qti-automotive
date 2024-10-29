FILESEXTRAPATHS:prepend := "${PATH_TO_REPO}/vendor/qcom/opensource/safelinux-system-cfg/irqbalance-config/:"

SRC_URI += "file://irqbalanced.conf \
            file://set_irq_bal_level.sh"

do_install:append() {
    install -d ${D}${sysconfdir}/systemd/system/irqbalanced.service.d
    install -m 0644 ${WORKDIR}/irqbalanced.conf -D ${D}${sysconfdir}/systemd/system/irqbalanced.service.d/99irqbalanced.conf
    install -m 0755 ${WORKDIR}/set_irq_bal_level.sh -D ${D}${bindir}/set_irq_bal_level.sh
}

