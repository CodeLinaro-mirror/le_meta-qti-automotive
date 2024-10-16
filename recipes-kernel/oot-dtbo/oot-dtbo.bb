SUMMARY = "External/Out of tree (OOT) device tree overlay"
DESCRIPTION = "External/out of tree (OOT) device tree overlay"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "bison-native dtc-native virtual/kernel"

FILESPATH =+ "${AUTOSOURCES}:"
SRC_URI = "\
           file://vendor/qcom/opensource/safelinux-system-cfg/devicetree \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/safelinux-system-cfg/devicetree"

inherit ark-dtb-merge deploy kernel-arch qti-techpack

EXTRA_OEMAKE += "KDIR=${STAGING_KERNEL_DIR}"
CONFIG_ARCH ?= ""
CONFIG_ARCH:sa7255 = "CONFIG_ARCH_SA7255=y"
IS_QCLINUX_BUILD = "${@bb.utils.contains_any("PREFERRED_PROVIDER_virtual/kernel", "linux-qcom-custom", "QCLINUX_BUILD=true", "", d)}"

do_compile() {
    make dtbos KDIR=${STAGING_KERNEL_DIR} O=${STAGING_KERNEL_BUILDDIR} ${CONFIG_ARCH} CC="${KERNEL_CC}" LD="${KERNEL_LD}" ${IS_QCLINUX_BUILD}
}

do_merge_dtb() {
    if [ -z "${KERNEL_BASE_DTB}" ]; then
        return 0
    fi

    install -d ${S}/out

    dtb_dir=${DEPLOY_DIR_IMAGE}/build-artifacts/kernel-dtb
    dtbo_dir=${S}
    out_dir=${S}/out
    merge_dtbos $dtb_dir $dtbo_dir $out_dir
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
    if [ -n "${OOT_DTBS}" ]; then
        install -d ${DEPLOYDIR}/build-artifacts/dtb

        for dtb in ${OOT_DTBS}; do
            if [ -f ${S}/$dtb ]; then
                install -m 0644 ${S}/$dtb ${DEPLOYDIR}/build-artifacts/dtb/
            fi
        done
    fi
}
addtask do_deploy after do_install
