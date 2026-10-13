SUMMARY = "Qualcomm devicetree blobs for gvm-gen4-5 and gvm-gen5"
DESCRIPTION = "Builds DTBs and DTBOs from qcom/opensource/devicetree \
against the linux-ack kernel."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://${STAGING_KERNEL_DIR}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

COMPATIBLE_MACHINE = "gvm-gen4-5|gvm-gen5"

inherit qti-techpack

DEPENDS += "dtc-aosp-native"

SRC_URI = "\
    ${PATH_TO_REPO}/kernel/kernel-${PV}/kernel_platform/qcom/opensource/devicetree/.git;protocol=${PROTO};destsuffix=kernel/kernel-${PV}/kernel_platform/qcom/opensource/devicetree;usehead=1 \
    "

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/kernel/kernel-${PV}/kernel_platform/qcom/opensource/devicetree"

# CONFIG variables passed to make dtbs to select the correct autogvm DTB targets.
# These are NOT in auto.conf (removed to prevent platformdlkm from picking up
# augen4soc.conf which enables CONFIG_PM_SILENT_MODE). Pass them explicitly here.
# Matches Bazel autogvmlv.bzl: CONFIG_ARCH_LEMANS=y, CONFIG_ARCH_MONACO_AUTO=y,
# CONFIG_ARCH_NORD=y, CONFIG_ARCH_QTI_VM=y.
QCOM_DTB_CONFIGS ?= "\
    CONFIG_ARCH_LEMANS=y \
    CONFIG_ARCH_MONACO_AUTO=y \
    CONFIG_ARCH_NORD=y \
    CONFIG_ARCH_QTI_VM=y \
"

do_configure[noexec] = "1"

# Depend on soc-modules:do_configure which transitively guarantees:
#   virtual/kernel:do_shared_workdir  -> auto.conf/autoconf.h generated
#   make-mod-scripts:do_configure     -> make prepare/syncconfig completed
#   make-mod-scripts:do_compile       -> kernel build scripts ready
# auto.conf is in its final stable state before DTB compilation reads it.
do_compile[depends] += "soc-modules:do_configure"

do_compile() {
    # Build base DTBs using the kernel build system's dtbs target.
    # dtstree must be relative to $(srctree) (kernel-source/) because the
    # kernel Makefile uses $(build)=$(dtstree) which prepends srctree.
    # An absolute path causes "kernel-source//absolute/path" double-path error.
    # Pass QCOM_DTB_CONFIGS on the command line so the qcom/Makefile selects
    # the correct autogvm-dtb-y targets. These CONFIGs are NOT in auto.conf
    # (removed to prevent platformdlkm from picking up augen4soc.conf).
    unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
    DTSTREE_REL=$(realpath --relative-to="${STAGING_KERNEL_DIR}" "${S}")
    oe_runmake -C ${STAGING_KERNEL_BUILDDIR} \
        dtstree=${DTSTREE_REL} \
        ${QCOM_DTB_CONFIGS} \
        DTC=${STAGING_KERNEL_BUILDDIR}/scripts/dtc/dtc \
        DTC_INCLUDE="${S}/scripts/dtc/include-prefixes/ ${STAGING_KERNEL_DIR}/scripts/dtc/include-prefixes/" \
        CC="${KERNEL_CC}" LD="${KERNEL_LD}" \
        HOSTCC="${BUILD_CC}" HOSTCFLAGS="${BUILD_CFLAGS}" \
        dtbs
}

do_install[noexec] = "1"

do_deploy() {
    # Deploy base DTBs to build-artifacts/dtb/ for qimage-boot.bbclass
    # to create dtb.img (merged into boot.img via mkbootimg --dtb).
    install -d ${DEPLOYDIR}/build-artifacts/dtb
    find ${S} -name "*.dtb" ! -name "*-overlay.dtb" | while read f; do
        install -m 0644 "$f" ${DEPLOYDIR}/build-artifacts/dtb/
    done

    # Deploy overlay DTBOs to build-artifacts/dtbo/ for qimage-boot.bbclass
    # to create dtbo.img (mkdtimg create dtbo.img dtbos/*.dtbo).
    install -d ${DEPLOYDIR}/build-artifacts/dtbo
    find ${S} -name "*.dtbo" | while read f; do
        install -m 0644 "$f" ${DEPLOYDIR}/build-artifacts/dtbo/
    done
}

addtask do_deploy after do_compile before do_build

PACKAGE_ARCH = "${MACHINE_ARCH}"
