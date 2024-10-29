SUMMARY = "provide display drivers header"
DESCRIPTION = "export display driver headers"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPL-2.0 WITH Linux-syscall-note"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/display-drivers/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/display-drivers;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/vendor/qcom/opensource/display-drivers/include/uapi"

DRM_UAPI_HEADERS = "\
    drm/msm_drm_pp.h \
    drm/sde_drm.h \
"

MEDIA_UAPI_HEADERS = "\
    media/mmm_color_fmt.h \
    media/msm_sde_rotator.h \
"

# There's nothing to do here, except install the headers where we can package them
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install[depends] += "virtual/kernel:do_shared_workdir"

do_install() {
   HEADER_INSTALL_TOOL=${STAGING_KERNEL_DIR}/scripts/headers_install.sh

   cd ${STAGING_KERNEL_BUILDDIR}

   install -d ${D}${includedir}/drm
   for h in ${DRM_UAPI_HEADERS}; do
        ${HEADER_INSTALL_TOOL} ${S}/display/$h ${D}${includedir}/$h
   done

   install -d ${D}${includedir}/media
   for h in ${MEDIA_UAPI_HEADERS}; do
        ${HEADER_INSTALL_TOOL} ${S}/display/$h ${D}${includedir}/$h
   done
}

ALLOW_EMPTY:${PN} = "1"
