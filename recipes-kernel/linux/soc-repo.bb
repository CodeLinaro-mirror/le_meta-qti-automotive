SUMMARY = "Qualcomm SoC kernel modules"
DESCRIPTION = "Out-of-tree kernel modules for Qualcomm SoC platforms"
HOMEPAGE = "https://www.codelinaro.org"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

COMPATIBLE_MACHINE = "qclinux-gvm-gen5"

# Kernel source dependency - must build after linux-qcom-rt
DEPENDS = "virtual/kernel linux-qcom-custom-rt"

PV = "1.0"

# Inherit kernel module class
inherit qti-techpack

# Disable automatic make clean in base_do_configure
CLEANBROKEN = "1"

# Source location
SRC_URI = "\
    ${PATH_TO_REPO}/vendor/qcom/opensource/soc-modules/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/soc-modules;usehead=1 \
    file://configs/soc-repo_defconfig \
"

SRCREV = "${AUTOREV}"

TECHPACK_MODULES = "\
    drivers/virt/gunyah/gh_dbl.ko \
    drivers/virt/gunyah/gh_msgq.ko \
    drivers/virt/gunyah/gh_rm_drv.ko \
    arch/arm64/gunyah/gh_arm_drv.ko \
    drivers/soc/qcom/hab/msm_hab.ko \
    drivers/virtio/virtio_mmio.ko \
    drivers/iommu/arm/arm-smmu/arm_smmu.ko \
    drivers/iommu/iommu-logger.ko \
    drivers/iommu/qcom_iommu_util.ko \
    drivers/soc/qcom/qcom_wdt_core.ko \
    drivers/soc/qcom/qcom_soc_wdt.ko \
    drivers/soc/qcom/minidump.ko \
    drivers/soc/qcom/debug_symbol.ko \
"

# Ensure we build after linux-qcom-rt shared workdir is ready
do_compile[depends] += "linux-qcom-custom-rt:do_shared_workdir"

# Source directory
S = "${WORKDIR}/vendor/qcom/opensource/soc-modules"
DEFCONFIG_FILE = "${WORKDIR}/configs/soc-repo_defconfig"

# Configure: Overlay headers
do_configure:append() {
    # Overlay soc-repo headers to kernel source to ensure they take precedence
    if [ -d ${S}/include ]; then
        cp -rf ${S}/include/* ${STAGING_KERNEL_DIR}/include/
    fi

    if [ -d ${S}/arch/arm64/include ]; then
        cp -rf ${S}/arch/arm64/include/* ${STAGING_KERNEL_DIR}/arch/arm64/include/
    fi
}

# Compile: Build modules using standard OOT approach
# CONFIG_VARS and DEFCONFIG_CFLAGS are passed explicitly to override any conflicting
# values that may already exist in auto.conf from the kernel's own Kconfig.
# For example, kernel may have CONFIG_ARM_SMMU=y (built-in) while soc-repo needs =m.
do_compile() {
    DEFCONFIG_CFLAGS=""
    CONFIG_VARS=""

    if [ -f ${DEFCONFIG_FILE} ]; then
        DEFCONFIG_CFLAGS=$(grep -E '^CONFIG_.*=[ym]$' ${DEFCONFIG_FILE} | \
                          sed 's/=.*//' | \
                          sed 's/^/-D/' | \
                          tr '\n' ' ')

        # Append int/hex type CONFIG values as -DCONFIG_FOO=value
        INT_CFLAGS=$(grep -E '^CONFIG_.*=[0-9]+$' ${DEFCONFIG_FILE} | \
                     sed 's/^\(CONFIG_[^=]*\)=\(.*\)$/-D\1=\2/' | \
                     tr '\n' ' ')
        DEFCONFIG_CFLAGS="${DEFCONFIG_CFLAGS} ${INT_CFLAGS}"

        CONFIG_VARS=$(grep -E '^CONFIG_.*=[ym]$' ${DEFCONFIG_FILE} | \
                     sed 's/=y/=m/' | \
                     tr '\n' ' ')
    else
        bbwarn "soc-repo_defconfig not found at ${DEFCONFIG_FILE}"
    fi

    oe_runmake -C ${STAGING_KERNEL_BUILDDIR} \
        M=${S} \
        ARCH=${ARCH} \
        CROSS_COMPILE=${TARGET_PREFIX} \
        ${CONFIG_VARS} \
        EXTRA_CFLAGS="-I${STAGING_KERNEL_DIR} ${DEFCONFIG_CFLAGS}" \
        KCFLAGS="-Wno-error=implicit-fallthrough -Wno-error=unused-variable -Wno-error=format -Wno-error=incompatible-pointer-types" \
        modules
}

# Exclude KERNEL_VERSION from task hash calculation to avoid metadata instability
# KERNEL_VERSION is read dynamically and may cause basehash changes during reparsing
do_patch[vardepsexclude] += "KERNEL_VERSION"
do_populate_sysroot[vardepsexclude] += "KERNEL_VERSION"
do_deploy_source_date_epoch[vardepsexclude] += "KERNEL_VERSION"
