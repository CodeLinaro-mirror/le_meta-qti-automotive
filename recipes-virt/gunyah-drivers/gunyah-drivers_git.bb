SUMMARY = "Gunyah Drivers Kernel Modules"
DESCRIPTION = "This is the gunyah driver used to communicate with RM and control GVM."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "virtual/kernel"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/gunyah-drivers/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/gunyah-drivers;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/gunyah-drivers"

TECHPACK_MODULES = "\
    arch/arm64/gunyah/gh_arm_drv.ko \
    drivers/virt/gunyah/gh_dbl.ko \
    drivers/virt/gunyah/gh_msgq.ko \
    drivers/virt/gunyah/gh_rm_drv.ko \
    drivers/virt/gunyah/gunyah.ko \
    drivers/tty/hvc/hvc_gunyah.ko \
"
inherit qti-techpack

do_install:append() {
    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
    install -d ${D}${libdir}/modules-load.d/
    install -m 0755 ${WORKDIR}/vendor/qcom/opensource/gunyah-drivers/gunyah-drivers.conf -D ${D}${libdir}/modules-load.d/gunyah-drivers.conf
    install -d ${D}${includedir}/linux/gunyah
    install -m 0644 ${WORKDIR}/vendor/qcom/opensource/gunyah-drivers/include/linux/gunyah/* ${D}${includedir}/linux/gunyah/
    install -m 0644 ${S}/Module.symvers ${D}${includedir}/
}

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*"
FILES:${PN} += "${libdir}/modules-load.d/*"

RPROVIDES:${PN} += "${@'kernel-module-gh-arm-drv-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-gh-msgq-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-hvc-gunyah-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-gh-rm-drv-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-gh-dbl-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-gunyah-${KERNEL_VERSION}'.replace('_', '-')}"
