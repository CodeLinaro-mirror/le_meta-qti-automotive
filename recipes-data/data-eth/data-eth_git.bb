SUMMARY = "Ethernet Data OOT Kernel Modules for PVM"
DESCRIPTION = "Out-of-tree kernel modules from the data-eth repository. \
               Builds eth_switch_monitor.ko for Ethernet switch monitoring on PVM."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DEPENDS = "gunyah-drivers"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/data-eth/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/data-eth;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/data-eth"

RDEPENDS:${PN} += "gunyah-drivers"

# Add one entry per module when new modules are introduced.
# qti-techpack installs each listed .ko to modules/${KERNEL_VERSION}/extra/.
TECHPACK_MODULES = "\
    drivers/eth_switch_monitor/eth_switch_monitor.ko \
"

inherit qti-techpack

# Required by drivers/eth_switch_monitor/Makefile.
TECHPACK_MAKE_ARGS = "GUNYAH_HEADERS=${STAGING_INCDIR}"
TECHPACK_MAKE_ARGS += "CONFIG_ETH_SWITCH_MONITOR=m"

# gunyah-drivers puts Module.symvers in ${includedir}/ instead of the standard location.
TECHPACK_MAKE_ARGS += "KBUILD_EXTRA_SYMBOLS=${STAGING_INCDIR}/Module.symvers"

# Install per-module modules-load.d configs if present.
do_install:append() {
    install -d ${D}${libdir}/modules-load.d/
    for conf_file in $(find ${S}/drivers/eth_switch_monitor -name "*.conf" 2>/dev/null); do
        install -m 0644 "$conf_file" ${D}${libdir}/modules-load.d/
    done
}

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*"
FILES:${PN} += "${libdir}/modules-load.d/*"

RPROVIDES:${PN} += "${@'kernel-module-eth-switch-monitor-${KERNEL_VERSION}'.replace('_', '-')}"
