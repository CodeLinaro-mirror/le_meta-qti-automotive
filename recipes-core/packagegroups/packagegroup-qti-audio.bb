SUMMARY = "QTI package group for audio"
DESCRIPTION = "This is the minimal set of packages required for audio kernel modules and audio initialization scripts."
LICENSE = "GPL-2.0 & BSD"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

PACKAGES = "${PN}"

# qti-audio is for elite framework, qti-audio-ar is for AR framework
# KMD means Kernel Mode Driver compare with UMD as User Mode Driver
# NOTE: For kernel 5.4 + AR + hyp, uses audiodlkm rather than ar-audiodlkm
KMD_RDEPENDS = "\
    alsa-lib \
    alsa-utils \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-audio', 'audiodlkm  init-audio', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-audio-ar', 'agm agm-client agm-alsa-plugin agm-server agm-sndparser init-audio', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-audio-ar', bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '', 'ar-audiodlkm', d), '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-audio-ar', oe.utils.version_less_or_equal('${preferred-kernel}', '5.14', '', 'ar-audiodlkm', d), '', d)} \
"

KMD_RDEPENDS:append:qti-dpk = " \
    agm-tinyalsa-plugin \
    ar-pal \
    pal-control-plugin \
    system-media \
    tinyalsa \
    tinycompress \
"

AUDIOLITE_RDEPENDS = "\
    audiolite-dlkm \
"

RDEPENDS:${PN} += "\
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-audio-aw', '${AUDIOLITE_RDEPENDS}', \
        bb.utils.contains('MACHINE_FEATURES', 'qti-gunyah qti-umd', '${AUDIOLITE_RDEPENDS} alsa-utils', '${KMD_RDEPENDS}', d), \
    d)} \
"
