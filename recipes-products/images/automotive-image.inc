IMAGE_INSTALL += "\
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-core', 'packagegroup-qti-core-minimal', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-display', 'packagegroup-qti-display', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-multimedia', bb.utils.contains('MACHINE_FEATURES', 'qti-umd', '', 'packagegroup-qti-multimedia', d), '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-security', 'packagegroup-qti-security', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-data', 'packagegroup-qti-data', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-vnw', 'packagegroup-qti-vnw', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-data', 'packagegroup-qti-data', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-extra', 'packagegroup-qti-extra', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-wlan', 'packagegroup-qti-wlan', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-tools', 'packagegroup-qti-tools', '', d)} \
    ${@bb.utils.contains_any('COMBINED_FEATURES', 'qti-audio qti-audio-ar qti-audio-aw', 'packagegroup-qti-audio', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-bluetooth', 'packagegroup-qti-bluetooth', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-location', 'packagegroup-qti-location-hal', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-camera', bb.utils.contains('MACHINE_FEATURES', 'qti-umd', '', 'packagegroup-qti-camera', d), '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-graphics', 'packagegroup-qti-graphics', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'packagegroup-qti-umd', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-vmm', 'packagegroup-qti-vmm', '', d)} \
    ${@bb.utils.contains('VARIANT', 'debug', 'packagegroup-qti-debug', '', d)} \
    "

IMAGE_INSTALL:append:sa8797 = " packagegroup-qti-multimedia"

IMAGE_LINGUAS = ""

EXTRA_IMAGECMD:ext4 = "-i 4096 -b 4096"

# default value for rootfs size
IMAGE_ROOTFS_SIZE ?= "1572864"

SSTATE_MANFILEPREFIX = "${@bb.utils.contains('PERF_BUILD', '1', '${SSTATE_MANIFESTS}/manifest-${SSTATE_MANMACH}-${PN}-perf', '${SSTATE_MANIFESTS}/manifest-${SSTATE_MANMACH}-${PN}' , d)}"

SDK_DEPLOY = "${DEPLOY_DIR}/sdk-${PRODUCT}"

# Add kernel header to SDK.
TOOLCHAIN_TARGET_TASK:append = " kernel-devsrc ${PREFERRED_PROVIDER_virtual/kernel-headers}-dev"

# Add libgomp header
TOOLCHAIN_TARGET_TASK:append = " libgomp-dev libgomp-staticdev"

BAD_RECOMMENDATIONS += "rng-tools"
