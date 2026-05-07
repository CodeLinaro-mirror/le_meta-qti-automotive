SUMMARY = "Graphics drivers"
DESCRIPTION = "Build graphics drivers to kernel module"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=8afb6abdac9a14cb18a0d6c9c151e9b4"

DEPENDS += "graphics-devicetree"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/graphics-kernel/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/graphics-kernel;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/graphics-kernel"

TECHPACK_MODULE_OUT = "${WORKDIR}/graphics-kernel"
TECHPACK_MODULES = "msm_kgsl.ko"
TECHPACK_HEADERS = "${S}/include/uapi"

inherit qti-techpack

do_install:append:sa81x5() {
    install -m 0644 ${S}/config/autoload_sa81x5.conf -D ${D}${sysconfdir}/modules-load.d/graphics_load.conf
}

do_install:append:monaco() {
    install -m 0644 ${S}/config/autoload_monaco_auto.conf -D ${D}${sysconfdir}/modules-load.d/graphics_load.conf
}

RPROVIDES:${PN} += "kernel-module-msm-kgsl-${KERNEL_VERSION}"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
FILES:${PN} += "${sysconfdir}/*"
