SUMMARY = "dsp kernel drivers"
DESCRIPTION = "Build dsp drivers to kernel module"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=8afb6abdac9a14cb18a0d6c9c151e9b4"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/dsp-kernel/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/dsp-kernel;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/dsp-kernel"

TECHPACK_MODULES = "frpc-adsprpc.ko cdsp-loader.ko"

inherit qti-techpack

RPROVIDES:${PN} += "kernel-module-frpc-adsprpc-${KERNEL_VERSION} \
                    kernel-module-cdsp-loader-${KERNEL_VERSION}"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
FILES:${PN} += "${sysconfdir}/*"
