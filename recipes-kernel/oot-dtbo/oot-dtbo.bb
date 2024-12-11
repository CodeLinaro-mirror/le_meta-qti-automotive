SUMMARY = "External/Out of tree (OOT) device tree overlay"
DESCRIPTION = "External/out of tree (OOT) device tree overlay"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "bison-native dtc-native virtual/kernel"

SRC_URI = "\
           ${PATH_TO_REPO}/vendor/qcom/opensource/safelinux-system-cfg/devicetree/.git;protocol=${PROTO};usehead=1 \
           ${PATH_TO_REPO}/kernel/${RH_KERNEL_NAME}/.git;protocol=${PROTO};usehead=1 \
           file://0001-devicetree-Adding-early-ramdisk-bootargs.patch \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/safelinux-system-cfg/devicetree"
OVERLAYED_OOT_DTBS_OUT = "${S}/out"

inherit ark-dtb-merge deploy kernel-arch qti-techpack

EXTRA_OEMAKE += "KDIR=${STAGING_KERNEL_DIR}"
CONFIG_ARCH ?= ""
CONFIG_ARCH:sa7255 = "CONFIG_ARCH_SA7255=y"
IS_QCLINUX_BUILD = "${@bb.utils.contains_any("PREFERRED_PROVIDER_virtual/kernel", "linux-qcom-custom linux-qcom-custom-rt", "QCLINUX_BUILD=true", "", d)}"

do_compile() {
    make dtbos KDIR=${STAGING_KERNEL_DIR} O=${STAGING_KERNEL_BUILDDIR} ${CONFIG_ARCH} CC="${KERNEL_CC}" LD="${KERNEL_LD}" ${IS_QCLINUX_BUILD}
}

do_merge_dtb() {
    if [ -z "${KERNEL_BASE_DTB}" ]; then
        return 0
    fi

    install -d ${OVERLAYED_OOT_DTBS_OUT}
    install -d ${S}/dtbodir

    if [ -z "${OOT_DTBOS}" ]; then
        OOT_DTBOS=$(find ${S} -not -path "${S}/oot/*" -name "*.dtbo" -printf "%P\n")
    fi

    for oot_dtbo in ${OOT_DTBOS}; do
        if [ -f ${S}/${oot_dtbo} ]; then
            install -m 0644 ${S}/${oot_dtbo} ${S}/dtbodir/
        fi
    done

    dtb_dir=${DEPLOY_DIR_IMAGE}/build-artifacts/kernel-dtb
    dtbo_dir=${S}/dtbodir
    merge_dtbos_single $dtb_dir $dtbo_dir ${OVERLAYED_OOT_DTBS_OUT}
}
do_merge_dtb[depends] += "virtual/kernel:do_deploy"
addtask do_merge_dtb after do_compile before do_install

do_install:append() {
    if [ -d ${S}/oot-dt-bindings/ ]; then
        install -d ${D}${includedir}/safelinux-system-cfg/oot-dt-bindings
        install -m 0644 ${S}/oot-dt-bindings/*.h ${D}${includedir}/safelinux-system-cfg/oot-dt-bindings/
    fi
}

OOT_DTBS ?= ""
do_deploy() {
    install -d ${DEPLOYDIR}/build-artifacts/dtb
    # Copy non-overlayed OOT DTBs, if OOT_DTBS supplied
    if [ -n "${OOT_DTBS}" ]; then
        for dtb in ${OOT_DTBS}; do
            if [ -f ${S}/$dtb ]; then
                install -m 0644 ${S}/$dtb ${DEPLOYDIR}/build-artifacts/dtb/
            fi
        done
    fi
    # Copy overlayed OOT DTBs, if generated post DTBO match
    if ls ${OVERLAYED_OOT_DTBS_OUT}/*.dtb 2>&1 > /dev/null; then
        install -m 0644 ${OVERLAYED_OOT_DTBS_OUT}/*.dtb ${DEPLOYDIR}/build-artifacts/dtb/
    fi
}
addtask do_deploy after do_install
