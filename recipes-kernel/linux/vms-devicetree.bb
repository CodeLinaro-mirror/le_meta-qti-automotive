DESCRIPTION = "Build kernel vms-devicetree overlays"
HOMEPAGE = "https://www.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "\
    file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
"

DEPENDS += "kernel-basedevicetree"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/vms-devicetree/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/vms-devicetree;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/vms-devicetree"

inherit deploy kernel-arch module-base

do_compile() {
    make dtbos \
        KDIR=${STAGING_KERNEL_DIR} \
        O=${STAGING_KERNEL_BUILDDIR} \
        CC="${KERNEL_CC}" \
        LD="${KERNEL_LD}" \
        OOT_DT_BINDINGS_DIR=${STAGING_DIR_TARGET}${includedir}
}

# lock to avoid parallel compiling with techpack
do_compile[lockfiles] += "${TMPDIR}/qti-techpack.lock"

do_deploy() {
    # Deploy all vms DDR overlays from build dir (second-level merge)
    if [ -n "$(ls ${B}/*.dtbo 2>/dev/null)" ]; then
        install -d ${DEPLOYDIR}/build-artifacts/vms-ddrdtbos
        install -m 0644 ${B}/*.dtbo \
            ${DEPLOYDIR}/build-artifacts/vms-ddrdtbos/
    fi
}

addtask do_deploy after do_compile before do_packagedata
