SUMMARY = "External Kernel Modules for Debug"
DESCRIPTION = "For building external kernel modules used for debugging"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

SYSTEMD_SERVICE:${PN} = "qcom-dcc.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/safelinux-dbg-modules/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/safelinux-dbg-modules;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/safelinux-dbg-modules"

TECHPACK_MODULES = "minidump/minidump.ko \
                    kaslr_store/kaslr_store.ko \
                    memory_dump_v2/memory_dump_v2.ko \
                    nhlos_log/dump_boot_log.ko \
                    cpuss_log/qcom_cpuss_log.ko \
                    firmware_vm_mini_dump/firmware_vm_mini_dump.ko \
"
TECHPACK_MODULES:append:gen5 = " memory_dump_v21/memory_dump_v21.ko"

inherit qti-techpack systemd
EXTRA_OEMAKE += "KDIR=${STAGING_KERNEL_DIR}"

EXTRA_OEMAKE:append:gen5 = " CONFIG_QCOM_MEMORY_DUMP_V21=y"

QCOM_DCC_CONF ?= "qcom_dcc_sa8775.conf"
QCOM_DCC_CONF:gen5 = "qcom_dcc_sa8797.conf"

do_install:append() {
    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
    install -d ${D}${libdir}/modules-load.d/
    install -m 0755 ${WORKDIR}/vendor/qcom/opensource/safelinux-dbg-modules/extern_dbg_mod.conf -D ${D}${libdir}/modules-load.d/extern_dbg_mod.conf

    install -d ${D}${bindir}
    install -m 0755 ${S}/qcom_dcc/qcom_dcc_config.sh -D ${D}${bindir}/qcom_dcc_config.sh
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/qcom_dcc/qcom-dcc.service -D ${D}/${systemd_unitdir}/system/qcom-dcc.service

    install -d ${D}/${sysconfdir}/qcom_dcc
    install -m 0755 ${S}/qcom_dcc/${QCOM_DCC_CONF} -D ${D}/${sysconfdir}/qcom_dcc/${QCOM_DCC_CONF}
}

RPROVIDES:${PN} += "${@'kernel-module-minidump-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-kaslr_store-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-memory_dump_v21-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-memory_dump_v2-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-dump_boot_log-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-qcom_cpuss_log-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-firmware_vm_mini_dump-${KERNEL_VERSION}'.replace('_', '-')}"

FILES:${PN} += "${bindir}/*"
FILES:${PN} += "${sysconfdir}/qcom_dcc/*"
FILES:${PN} += "${libdir}/modules-load.d/*"
FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*"
