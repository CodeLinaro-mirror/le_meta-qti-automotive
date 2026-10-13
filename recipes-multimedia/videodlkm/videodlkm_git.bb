SUMMARY = "video driver"
DESCRIPTION = "Recipe to build video driver module. The driver configures the vidc interfaces in the video subsystem."
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=8afb6abdac9a14cb18a0d6c9c151e9b4"

DEPENDS += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '', 'video-devicetree', d)}"
DEPENDS:gvm-gen5 += "video-devicetree virtio-video kernel-module-msm-virtio-video"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/video-driver/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/video-driver;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/video-driver"
EXT_MODULE = "vendor/qcom/opensource/video-driver"

TECHPACK_MODULE_OUT = "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '', '${WORKDIR}/vendor/qcom/opensource/video-driver-out', d)}"
TECHPACK_MODULE_OUT:gvm-gen5 = "${S}"

TECHPACK_MODULES = "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '', 'msm-vidc.ko', d)}"
TECHPACK_MODULES:gvm-gen5 = "msm_video.ko"

TECHPACK_MAKE_ARGS:gvm-gen5 = "\
    BOARD_PLATFORM=gen5 \
    ENABLE_HYP=true \
"

TECHPACK_HEADERS = "${S}/include/uapi"

inherit qti-techpack

# Copy vidc_hw_virt.h from the sysroot into the kernel source tree before
# compiling. do_configure:prepend in virtio-video wrote it there at build time,
# but that path is a side-effect not tracked by sstate. Reading it here from
# the sysroot (installed by virtio-video:do_install) is sstate-safe.
do_compile:prepend:gvm-gen5() {
    install -D -m 0644 \
        "${STAGING_INCDIR}/kernel-module-msm-virtio-video/vidc_hw_virt.h" \
        "${STAGING_KERNEL_DIR}/include/vidc_hw_virt.h"
}

RDEPENDS:${PN}:gvm-gen5 += "kernel-module-msm-virtio-video-${KERNEL_VERSION}"
RDEPENDS:${PN}:gvm-gen4-5 += "kernel-module-msm-virtio-video-${KERNEL_VERSION}"

RPROVIDES:${PN} += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '', 'kernel-module-msm-vidc-${KERNEL_VERSION}', d)}"
RPROVIDES:${PN}:gvm-gen5 += "kernel-module-msm-video-${KERNEL_VERSION}"

FILES:${PN} += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '', '${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*', d)}"
FILES:${PN}:gvm-gen5 += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/msm_video.ko"

ALLOW_EMPTY:${PN} = "1"
