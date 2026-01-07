SUMMARY = "Install virtio-video uapi headers"
DESCRIPTION = "This contains headers userspace API"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=8afb6abdac9a14cb18a0d6c9c151e9b4"

SRC_URI = "\
    ${PATH_TO_REPO}/vendor/qcom/opensource/virtio-video/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/virtio-video;usehead=1 \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/virtio-video"

do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[depends] += "virtual/kernel:do_shared_workdir"

do_install:append() {
    install -d -p ${D}${includedir}/
    cd ${STAGING_KERNEL_BUILDDIR}
    ${STAGING_KERNEL_DIR}/scripts/headers_install.sh ${S}/include/virtio_video.h ${D}${includedir}/virtio_video.h
    if [ -f ${S}/include/virtio_video_msm_ext.h ]; then
       ${STAGING_KERNEL_DIR}/scripts/headers_install.sh ${S}/include/virtio_video_msm_ext.h ${D}${includedir}/virtio_video_msm_ext.h
    fi
    if [ -f ${S}/include/virtio_video_hw_virt.h ]; then
       ${STAGING_KERNEL_DIR}/scripts/headers_install.sh ${S}/include/virtio_video_hw_virt.h ${D}${includedir}/virtio_video_hw_virt.h
    fi
}

ALLOW_EMPTY:${PN} = "1"
