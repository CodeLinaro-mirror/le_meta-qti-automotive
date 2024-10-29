FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:remove = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", "file://syslog.cfg", "", d)}"
SRC_URI:append = " \
    ${@bb.utils.contains("DISTRO_FEATURES", "systemd", "file://disable-klog.cfg", "", d)} \
    ${@bb.utils.contains("MACHINE_SUPPORTS_INIT_RAMDISK", "True", "file://init.cfg", "", d)} \
    ${@bb.utils.contains("MACHINE_SUPPORTS_INIT_RAMDISK", "True", "file://installer.cfg", "", d)} \
"

FILES:${PN}-syslog += "${systemd_unitdir}/system/busybox-syslog.service"
