SRC_URI:append = " file://0001-Subject-PATCH-2-6-volume-ramp-additions-to-the-low-l.patch"
SRC_URI:append = " file://0001-Subject-PATCH-5-6-sink-input-volume-Add-support-for-.patch"
SRC_URI:append = " file://0003-volume-ramp-adding-volume-ramping-to-sink-input.patch"
SRC_URI:append = " file://0004-sink-input-Code-cleanup-regarding-volume-ramping.patch"
SRC_URI:append = " file://0001-pulseaudio-module-devel-an-interface-for-module-deve.patch"
SRC_URI:append = " file://0001-disable-timer-based-scheduling.patch"
SRC_URI:append = " file://0002-default.pa-Load-acdb-and-codec-control-modules.patch"
SRC_URI:append = " file://0007-stream-event-extension.patch"
SRC_URI:append = " file://0008-Pulseaudio-service-need-to-wait-for-sound-card-ready.patch"
SRC_URI:append = " file://0003-default.pa-Load-agl-audio-plugin-module.patch"
SRC_URI:append = " file://0001-Support-PulseAudio-Client-API-for-Module-Codec-Contr.patch"
SRC_URI:append = " file://0001-pulseaudio-config-default.pa-to-disable-default-ALSA.patch"
SRC_URI:append = " file://0001-udev-bypass-udev-device-enumeration-for-auto-targets.patch"
SRC_URI:append = " file://0001-Avoid-pulseaudio-daemon-shutdown-after-lpm.patch"
SRC_URI:append = " file://0001-Pulseaudio-service-increase-TimeoutStopSec-and-KillM.patch"
SRC_URI:append = " file://0001-monitor-soundcard-to-recover-init-state.patch"
SRC_URI:append = " ${@bb.utils.contains('MACHINE_FEATURES', 'qti-audio-ar','file://0001-disable-realtime-schedule-in-pulseaudio.patch','',d)}"

RDEPENDS:pulseaudio-server += "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'pulseaudio-module-systemd-login', '', d)} \
"

do_install:append() {
    # Install pulseaudio systemd service
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        # Execute these manually on behalf of systemctl script (from systemd-systemctl-native.bb)
        # because it does not support systemd's user mode.
        install -d ${D}${systemd_user_unitdir}/sockets.target.wants/
        ln -sf ${systemd_user_unitdir}/pulseaudio.socket ${D}${systemd_user_unitdir}/sockets.target.wants/

        install -d ${D}${systemd_user_unitdir}/default.target.wants/
        ln -sf ${systemd_user_unitdir}/pulseaudio.service ${D}${systemd_user_unitdir}/default.target.wants/
    fi
}

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:${THISDIR}/${BPN}-${PV}:"
