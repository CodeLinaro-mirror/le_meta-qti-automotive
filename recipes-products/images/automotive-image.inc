IMAGE_LINGUAS = ""

EXTRA_IMAGECMD:ext4 = "-i 4096 -b 4096"

# default value for rootfs size
IMAGE_ROOTFS_SIZE ?= "1572864"

SSTATE_MANFILEPREFIX = "${@bb.utils.contains('PERF_BUILD', '1', '${SSTATE_MANIFESTS}/manifest-${SSTATE_MANMACH}-${PN}-perf', '${SSTATE_MANIFESTS}/manifest-${SSTATE_MANMACH}-${PN}' , d)}"

SDK_DEPLOY = "${DEPLOY_DIR}/sdk-${PRODUCT}"

# Add kernel header to SDK.
# TOOLCHAIN_TARGET_TASK:append = " kernel-devsrc ${PREFERRED_PROVIDER_virtual/kernel}-headers-dev"

# Add libgomp header
TOOLCHAIN_TARGET_TASK:append = " libgomp-dev libgomp-staticdev"

BAD_RECOMMENDATIONS += "rng-tools"
