SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/agl-audio-plugin/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/agl-audio-plugin;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/vendor/qcom/opensource/agl-audio-plugin"

EXTRA_OECMAKE += "\
    -DPULSEAUDIO_VERSION=17.0 \
    -DTARGET_BOARD_PLATFORM=${BASEMACHINE} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-audio-ar', '-DAUDIO_ARCH=audio_reach', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-audio-awe', '-DAUDIO_ARCH=audio_awe', '', d)} \
    "
FILES:${PN} += "${libdir}/pulseaudio/modules/*"
FILES:${PN}-dbg += "${libdir}/pulseaudio/modules/.debug/*"
