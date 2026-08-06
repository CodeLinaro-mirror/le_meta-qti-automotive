SUMMARY = "Graphics hgsl drivers"
DESCRIPTION = "Build graphics hgsl drivers to kernel module"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=8afb6abdac9a14cb18a0d6c9c151e9b4"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/graphics-hgsl/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/graphics-hgsl;usehead=1 \
           file://autogvm_hgsl_load.conf"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/graphics-hgsl"

EXT_MODULE = "vendor/qcom/opensource/graphics-hgsl"

TECHPACK_MODULE_OUT = "${WORKDIR}/graphics-hgsl"
TECHPACK_MODULES = "qcom_hgsl.ko"
TECHPACK_HEADERS = "${S}/include/uapi"

inherit qti-techpack

do_install:append() {
    install -m 0644 ${WORKDIR}/autogvm_hgsl_load.conf -D ${D}${sysconfdir}/modules-load.d/autogvm_hgsl_load.conf
    install -d ${D}${includedir}/linux
    install -m 0644 ${S}/include/uapi/linux/hgsl.h ${D}${includedir}/linux
}

RPROVIDES:${PN} += "kernel-module-qcom-hgsl-${KERNEL_VERSION}"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
FILES:${PN} += "${sysconfdir}/*"
