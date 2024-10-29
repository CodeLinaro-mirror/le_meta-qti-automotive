SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/agl-audio-plugin/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/agl-audio-plugin;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/vendor/qcom/opensource/agl-audio-plugin"

EXTRA_OECMAKE += "\
    -DTARGET_BOARD_PLATFORM=${BASEMACHINE} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-audio-ar', '-DAUDIO_ARCH=audio_reach', '', d)} \
    ${@bb.utils.contains('LAYERSERIES_COMPAT_yocto', 'kirkstone', '-DPULSEAUDIO_VERSION=15.0', '', d)} \
    "
FILES:${PN} += "${libdir}/pulse-*/modules/*"
FILES:${PN}-dbg += "${libdir}/pulse-*/modules/.debug/*"
