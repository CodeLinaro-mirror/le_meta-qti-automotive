SUMMARY = "Qualcomm DTC tools including fdtoverlaymerge"
DESCRIPTION = "Builds fdtoverlaymerge from external/qcom-dtc for merging DTB overlays."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-or-later | BSD-2-Clause"
LIC_FILES_CHKSUM = "file://GPL;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://BSD-2-Clause;md5=5d6306d1b08f8df623178dfd81880927"

# linux-ack and linux-ack-rt both use the ACK kernel source tree, so pick the
# matching PREFERRED_VERSION_* for whichever provider is active; anything
# else (e.g. linux-msm) falls back to the linux-msm kernel version.
python () {
    provider = d.getVar('PREFERRED_PROVIDER_virtual/kernel')
    if provider in ('linux-ack', 'linux-ack-rt'):
        kernel_ver = d.getVar('PREFERRED_VERSION_%s' % provider)
    else:
        kernel_ver = d.getVar('PREFERRED_VERSION_linux-msm')
    d.setVar('KERNEL_VER', kernel_ver)
}
BASE_GIT_PATH = "${PATH_TO_REPO}/kernel/kernel-${KERNEL_VER}/kernel_platform"
BASE_PATH = "kernel/kernel-${KERNEL_VER}/kernel_platform"

SRC_URI = "${BASE_GIT_PATH}/external/qcom-dtc/.git;protocol=${PROTO};destsuffix=${BASE_PATH}/external/qcom-dtc;usehead=1 \
           "

SRCREV = "${AUTOREV}"

S = "${WORKDIR}"

inherit native

do_configure[noexec] = "1"

do_compile() {
    QCOM_DTC="${WORKDIR}/${BASE_PATH}/external/qcom-dtc"
    ${BUILD_CC} ${BUILD_CFLAGS} \
        -I${QCOM_DTC} \
        -I${QCOM_DTC}/libfdt \
        ${QCOM_DTC}/fdtoverlaymerge.c \
        ${QCOM_DTC}/util.c \
        ${QCOM_DTC}/libfdt/fdt.c \
        ${QCOM_DTC}/libfdt/fdt_ro.c \
        ${QCOM_DTC}/libfdt/fdt_rw.c \
        ${QCOM_DTC}/libfdt/fdt_sw.c \
        ${QCOM_DTC}/libfdt/fdt_wip.c \
        ${QCOM_DTC}/libfdt/fdt_overlay.c \
        ${QCOM_DTC}/libfdt/fdt_addresses.c \
        ${QCOM_DTC}/libfdt/fdt_empty_tree.c \
        ${QCOM_DTC}/libfdt/fdt_strerror.c \
        -DNO_YAML \
        -o ${WORKDIR}/fdtoverlaymerge
}

do_install() {
    install -d ${D}/${bindir}
    install -m 0755 ${WORKDIR}/fdtoverlaymerge ${D}/${bindir}/fdtoverlaymerge
}
