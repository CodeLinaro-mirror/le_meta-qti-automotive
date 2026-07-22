FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append = " \
             ${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '5.15', 'file://0032-systemd-add-bootkpi-marker-for-user-session.patch', '', d)} \
             ${@bb.utils.contains('DISTRO_FEATURES', 'early_init', 'file://0034-systemd-add-handover-support-for-early-service.patch', '', d)} \
             file://power-switch.rules \
             file://0001-journal-replace-a-bunch-of-assert-with-friendlier-ch.patch \
             file://0001-journald-disable-audit-support-completely-from-the-j.patch \
             file://0036-systemd-journald-optimize-kmsg-reading-performance.patch \
             file://0001-systemd-add-vfio-script-wait-in-systemd-init-process.patch \
             file://0001-udev-make-symlink-related-rules-be-triggered-earlier.patch \
             file://qti_sleep.sh \
             ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'file://0037-systemd-Add-wdt_ping-in-dispatch_runqueue.patch', '', d)} \
             ${@bb.utils.contains('DISTRO_FEATURES', 'qti-rumi', 'file://0031-udev-trigger-only-enable-must-part-while-leave-other.patch', '', d)} \
             ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'file://0001-systemd-sleep-change-suspend-state-list.patch', '', d)} \
             ${@bb.utils.contains('MACHINE_FEATURES', 'deepsleep', 'file://0002-systemd-add-deepsleep-support.patch', '', d)} \
             file://0035-systemd-Change-job-print-method-to-Normal-way.patch \
             ${@bb.utils.contains('MACHINE_FEATURES', 'early-ramdisk-init', 'file://0001-systemd-Change-systemd-modules-load-service-type-to-.patch', '', d)} \
             ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'file://0001-systemd-shutdown-shorten-file-sync-timeout.patch', '', d)} \
             ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'file://0001-systemd-Remove-systemd-watchdog-ping-condition.patch', '', d)} \
"

SRC_URI:append:gen5 = " \
             file://0001-systemd-add-mm-vfio-script-wait-in-systemd-init-proc.patch \
"

do_install:append() {
   install -d ${D}/${base_libdir}/systemd/system-sleep
   install -m 0755 ${WORKDIR}/qti_sleep.sh -D ${D}/${base_libdir}/systemd/system-sleep/qti_sleep.sh
}
