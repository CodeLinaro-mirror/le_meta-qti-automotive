SUMMARY = "Platform kernel drivers"
DESCRIPTION = "Build platform drivers to kernel module"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPL-2.0 WITH Linux-syscall-note"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "\
    ${PATH_TO_REPO}/vendor/qcom/opensource/dsp-kernel/.git;protocol=${PROTO};name=dspkernel;destsuffix=vendor/qcom/opensource/dsp-kernel;usehead=1 \
    ${PATH_TO_REPO}/vendor/qcom/opensource/platform-kernel/.git;protocol=${PROTO};name=platformkernel;destsuffix=vendor/qcom/opensource/platform-kernel;;usehead=1 \
"
SRCREV_dspkernel = "${AUTOREV}"
SRCREV_platformkernel = "${AUTOREV}"
SRCREV_FORMAT = "dspkernel_platformkernel"

S = "${WORKDIR}/vendor/qcom/opensource/platform-kernel"

METAL_MODULES_BUILD = "drivers/aop-set-ddr.ko drivers/silent_boot.ko drivers/wallpower_charger.ko drivers/dump_boot_log.ko drivers/silent-mode-hw-monitoring.ko"

VIRT_MODULES_BUILD = "drivers/socinfo_dt.ko drivers/subsystem_notif_virt.ko"
VIRT_MODULES_BUILD:append:quin-gvm-gen4 = " drivers/virtual_fastrpc/vfastrpc.ko"
VIRT_MODULES_BUILD:append:quin-gvm-lemans = " drivers/virtual_fastrpc/hfastrpc.ko"
VIRT_MODULES_BUILD:append:quin-gvm-monaco = " drivers/virtual_fastrpc/hfastrpc.ko"

TECHPACK_MODULES = "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '${VIRT_MODULES_BUILD}', '${METAL_MODULES_BUILD}', d)}"

inherit qti-techpack

METAL_PROVIDES_MODULES = "\
    kernel-module-aop-set-ddr-${KERNEL_VERSION} \
    kernel-module-silent-boot-${KERNEL_VERSION} \
    kernel-module-wallpower-charger-${KERNEL_VERSION} \
    kernel-module-dump-boot-log-${KERNEL_VERSION} \
    kernel-module-silent-mode-hw-monitoring-${KERNEL_VERSION} \
"

VIRT_PROVIDES_MODULES = "\
    kernel-module-socinfo-dt-${KERNEL_VERSION} \
    kernel-module-subsystem-notif-virt-${KERNEL_VERSION} \
"
VIRT_PROVIDES_MODULES:append:quin-gvm-gen4 = " kernel-module-vfastrpc-${KERNEL_VERSION}"
VIRT_PROVIDES_MODULES:append:quin-gvm-lemans = " kernel-module-hfastrpc-${KERNEL_VERSION}"
VIRT_PROVIDES_MODULES:append:quin-gvm-monaco = " kernel-module-hfastrpc-${KERNEL_VERSION}"

do_configure:prepend() {
    ln -sf ${WORKDIR}/vendor/qcom/opensource/dsp-kernel/dsp/adsprpc_compat.h ${S}/drivers/virtual_fastrpc/dsp/adsprpc_compat.h
    ln -sf ${WORKDIR}/vendor/qcom/opensource/dsp-kernel/dsp/adsprpc_shared.h ${S}/drivers/virtual_fastrpc/dsp/adsprpc_shared.h
    ln -sf ${WORKDIR}/vendor/qcom/opensource/dsp-kernel/dsp/fastrpc_trace.h ${S}/drivers/virtual_fastrpc/dsp/fastrpc_trace.h
    ln -sf ${WORKDIR}/vendor/qcom/opensource/dsp-kernel/include/uapi/fastrpc_shared.h ${S}/drivers/virtual_fastrpc/dsp/fastrpc_shared.h
    ln -sf ${WORKDIR}/vendor/qcom/opensource/dsp-kernel/dsp/adsprpc_compat.c ${S}/drivers/virtual_fastrpc/dsp/adsprpc_compat.c
    ln -sf ${WORKDIR}/vendor/qcom/opensource/dsp-kernel/dsp/adsprpc_rpmsg.c ${S}/drivers/virtual_fastrpc/dsp/adsprpc_rpmsg.c
}

do_install:append:quin-gvm-lemans() {
    install -m 0755 ${S}/drivers/virtual_fastrpc/fastrpc_load.conf -D ${D}${sysconfdir}/modules-load.d/fastrpc_load.conf
}

RPROVIDES:${PN} += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '${VIRT_PROVIDES_MODULES}', '${METAL_PROVIDES_MODULES}', d)}"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
FILES:${PN} += "${sysconfdir}/*"
