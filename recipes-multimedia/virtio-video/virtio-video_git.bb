SUMMARY = "virtio video driver"
DESCRIPTION = "Recipe to build virtio video driver module provides BE communication"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=8afb6abdac9a14cb18a0d6c9c151e9b4"

VIRTIO_SRC ?= "${TARGET_DIR}vendor/qcom/opensource/virtio-video"
VIRTIO_SRC:gvm-gen5 = "${TARGET_DIR}vendor/qcom/opensource/virtio-video/gen5"
VIRTIO_SRC:gvm-gen4-5 = "${TARGET_DIR}vendor/qcom/opensource/virtio-video/gen4"
SRC_URI = "${PATH_TO_REPO}/${VIRTIO_SRC}/.git;protocol=${PROTO};destsuffix=${VIRTIO_SRC};usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/${VIRTIO_SRC}"

EXT_MODULE = "${VIRTIO_SRC}"

TECHPACK_MODULE_OUT = "${S}"
TECHPACK_MODULES = "msm_virtio_video.ko"

TECHPACK_MAKE_ARGS = "\
    VIDEO_ROOT=${S} \
    ENABLE_HYP=true \
    BOARD_PLATFORM=gen5 \
    MODNAME=msm_virtio_video \
"

inherit qti-techpack

do_configure[depends] += "virtual/kernel:do_shared_workdir"

# copy vidc_hw_virt.h into the kernel source tree for this and downstream videodlkm build
do_configure:prepend() {
    install -D -m 0644 \
        "${S}/include/vidc_hw_virt.h" \
        "${STAGING_KERNEL_DIR}/include/vidc_hw_virt.h"
}

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/msm_virtio_video.ko"

RPROVIDES:${PN} += "kernel-module-msm-virtio-video-${KERNEL_VERSION}"
