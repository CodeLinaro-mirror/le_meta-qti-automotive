SUMMARY = "MSM VIRTIO SND"
DESCRIPTION = "virtio sndcard."
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"
DEPENDS += "virtual/kernel"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/audio-kernel-virtio/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/audio-kernel-virtio;usehead=1 \
            file://msm-virtio-snd-load.conf \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/audio-kernel-virtio"

EXT_MODULE = "vendor/qcom/opensource/audio-kernel-virtio"
TECHPACK_MODULES = "msm_virtio_snd.ko"
inherit qti-techpack

do_install:append() {
    install -d -m 0755 ${D}${sysconfdir}/modules-load.d
    install -m 0644 ${WORKDIR}/msm-virtio-snd-load.conf ${D}${sysconfdir}/modules-load.d/msm-virtio-snd-load.conf
}

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/msm_virtio_snd.ko"
FILES:${PN} += "${sysconfdir}/modules-load.d/*"

RPROVIDES:${PN} += "kernel-module-msm-virtio-snd-${KERNEL_VERSION}"
