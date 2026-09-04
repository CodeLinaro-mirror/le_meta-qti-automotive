SUMMARY = "Qualcomm devicetree for qclinux-gvm-gen5"
DESCRIPTION = "To provide devicetree attributes for gvm kernel"
HOMEPAGE = "https://www.codelinaro.org"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

COMPATIBLE_MACHINE = "qclinux-gvm-gen5"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/soc-modules/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/soc-modules;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/soc-modules/devicetree"

CLEANBROKEN = "1"

inherit qti-techpack

TECHPACK_DTBS = "sa8797p-v2-gunyah-vm-qclv-qam.dtb"
TECHPACK_DTBOS = "sa8797p-gunyah-vm-lv-qam-overlay.dtbo"

# The devicetree/qcom/Makefile already exists in the source tree.
# No configure step needed.
do_configure[noexec] = "1"

# Depend on soc-repo:do_configure to ensure kernel headers and auto.conf
# are fully prepared before DTB compilation reads them.
do_compile[depends] += "soc-repo:do_configure"

# Override qti-techpack's module_do_compile.
# DTB compilation requires driving from the kernel top-level Makefile
# (-C kernel-source O=KBA) with dtstree pointing to the DTS directory.
# Using -C KBA M=... (OOT module style) has no 'dtbs' rule and fails.
# dtstree must be relative to STAGING_KERNEL_DIR because the kernel
# Makefile uses $(build)=$(dtstree) which prepends srctree internally.
# Build only the targets listed in TECHPACK_DTBS/TECHPACK_DTBOS to avoid
# compiling unrelated DTBs that may have missing header dependencies.
# always-y targets are declared in ${S}/Makefile directly (no dynamic append needed).
do_compile() {
    unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
    DTSTREE_REL=$(realpath --relative-to="${STAGING_KERNEL_DIR}" "${S}")
    oe_runmake -C ${STAGING_KERNEL_DIR} \
        O=${STAGING_KERNEL_BUILDDIR} \
        dtstree=${DTSTREE_REL} \
        DTC=${STAGING_KERNEL_BUILDDIR}/scripts/dtc/dtc \
        CC="${KERNEL_CC}" LD="${KERNEL_LD}" \
        ${TECHPACK_DTBS} ${TECHPACK_DTBOS}
}

do_deploy() {
    install -d ${DEPLOYDIR}/build-artifacts/dtb
    install -d ${DEPLOYDIR}/build-artifacts/dtbo

    for dtb in ${TECHPACK_DTBS}; do
        if [ -f ${S}/$dtb ]; then
            install -m 0644 ${S}/$dtb ${DEPLOYDIR}/build-artifacts/dtb/
        fi
    done

    for dtbo in ${TECHPACK_DTBOS}; do
        if [ -f ${S}/$dtbo ]; then
            install -m 0644 ${S}/$dtbo ${DEPLOYDIR}/build-artifacts/dtbo/
        fi
    done
}

