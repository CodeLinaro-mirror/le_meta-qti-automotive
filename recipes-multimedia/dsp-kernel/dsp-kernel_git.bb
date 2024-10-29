SUMMARY = "dsp kernel drivers"
DESCRIPTION = "Build dsp drivers to kernel module"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPL-2.0 WITH Linux-syscall-note"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/dsp-kernel/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/dsp-kernel;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/dsp-kernel"

TECHPACK_MODULES = "frpc-adsprpc.ko cdsp-loader.ko"

inherit qti-techpack

RPROVIDES:${PN} += "kernel-module-frpc-adsprpc-${KERNEL_VERSION} \
                    kernel-module-cdsp-loader-${KERNEL_VERSION}"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
FILES:${PN} += "${sysconfdir}/*"
