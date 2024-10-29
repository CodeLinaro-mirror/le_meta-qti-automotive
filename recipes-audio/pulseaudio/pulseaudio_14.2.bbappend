SRC_URI:append = " https://git.codelinaro.org/clo/le/AGL/meta-agl/-/raw/automotivelinux/eel/meta-ivi-common/recipes-multimedia/pulseaudio/pulseaudio-10.0/0002-volume-ramp-additions-to-the-low-level-infra.patch;downloadfilename=0002-volume-ramp-additions-to-the-low-level-infra.patch;name=infra-p"
SRC_URI[infra-p.sha256sum] = "085d2dd1c778f5a2fd76fd0b7a8a65f15493e0096bde0ddc96d276dda4753c91"

SRC_URI:append = " https://git.codelinaro.org/clo/le/AGL/meta-agl/-/raw/automotivelinux/eel/meta-ivi-common/recipes-multimedia/pulseaudio/pulseaudio-10.0/0003-volume-ramp-adding-volume-ramping-to-sink-input.patch;downloadfilename=0003-volume-ramp-adding-volume-ramping-to-sink-input.patch;name=input-p"
SRC_URI[input-p.sha256sum] = "4f8f0f3693d24cba2c9408550766f660e75aa228ba2c1fa8df0d7ae71ecb1831"

SRC_URI:append = " https://git.codelinaro.org/clo/le/AGL/meta-agl/-/raw/automotivelinux/eel/meta-ivi-common/recipes-multimedia/pulseaudio/pulseaudio-10.0/0004-sink-input-Code-cleanup-regarding-volume-ramping.patch;downloadfilename=0004-sink-input-Code-cleanup-regarding-volume-ramping.patch;name=ramping-p"
SRC_URI[ramping-p.sha256sum] = "727e3b9bf87ffb3fb8eeaafda15cb627731cf366a6d97a141fdfea01f05aac81"

SRC_URI:append = " https://git.codelinaro.org/clo/le/AGL/meta-agl/-/raw/automotivelinux/eel/meta-ivi-common/recipes-multimedia/pulseaudio/pulseaudio-10.0/0005-sink-input-volume-Add-support-for-volume-ramp-factor.patch;downloadfilename=0005-sink-input-volume-Add-support-for-volume-ramp-factor.patch;name=factor-p"
SRC_URI[factor-p.sha256sum] = "493c5179598fe852014e00443795a6e8b0cfdddffe60133027dac449c2730d5a"

SRC_URI:append = " file://0001-pulseaudio-module-devel-an-interface-for-module-deve.patch"
SRC_URI:append = " file://0001-disable-timer-based-scheduling.patch"
SRC_URI:append = " file://0002-default.pa-Load-acdb-and-codec-control-modules.patch"
SRC_URI:append = " file://0007-stream-event-extension.patch"
SRC_URI:append = " file://0008-Pulseaudio-service-need-to-wait-for-sound-card-ready.patch"
SRC_URI:append = " file://0003-default.pa-Load-agl-audio-plugin-module.patch"
SRC_URI:append = " file://0001-Support-PulseAudio-Client-API-for-Module-Codec-Contr.patch"
SRC_URI:append = " file://0001-pulseaudio-config-default.pa-to-disable-default-ALSA.patch"
SRC_URI:append = " file://0001-udev-bypass-udev-device-enumeration-for-auto-targets.patch"

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
