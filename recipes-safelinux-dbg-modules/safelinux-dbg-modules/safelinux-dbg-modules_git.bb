SUMMARY = "External Kernel Modules for Debug"
DESCRIPTION = "For building external kernel modules used for debugging"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/safelinux-dbg-modules/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/safelinux-dbg-modules;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/safelinux-dbg-modules"

TECHPACK_MODULES = "minidump/minidump.ko \
                    kaslr_store/kaslr_store.ko \
                    memory_dump_v2/memory_dump_v2.ko \
                    xbl_log/dump_boot_log.ko \
"
inherit qti-techpack
EXTRA_OEMAKE += "KDIR=${STAGING_KERNEL_DIR}"

do_install:append() {
    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
    install -d ${D}${libdir}/modules-load.d/
    install -m 0755 ${WORKDIR}/vendor/qcom/opensource/safelinux-dbg-modules/extern_dbg_mod.conf -D ${D}${libdir}/modules-load.d/extern_dbg_mod.conf
}

RPROVIDES:${PN} += "${@'kernel-module-minidump-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-kaslr_store-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-memory_dump_v2-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-dump_boot_log-${KERNEL_VERSION}'.replace('_', '-')}"

FILES:${PN} += "${libdir}/modules-load.d/*"
FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*"
