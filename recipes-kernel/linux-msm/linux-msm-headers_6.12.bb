SUMMARY = "CLO Linux Kernel Headers"
DESCRIPTION = "Installs MSM kernel headers required to build userspace. \
These headers are installed in ${includedir}/linux-msm path."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

FILESPATH =+ "${SRC_DIR_ROOT}/kernel:"

PROVIDES += "virtual/kernel-headers"

SRC_URI = "file://kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform/common"

S = "${WORKDIR}/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform/common"
KERNEL_BUILD_VARIANT = "${@bb.utils.contains_any('VARIANT', 'perf user', '', 'debug-', d)}"
KERNEL_OUT_VARIANT = "${@bb.utils.contains_any('VARIANT', 'perf user', '', 'debug_', d)}"

inherit kernel-arch pkgconfig multilib_header

BZ_PREBUILT_ROOT = "${SRC_DIR_ROOT}/kernel/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_populate_kernel_header_artifacts() {
    mkdir -p ${B}/headers
    cp -a ${BZ_PREBUILT_ROOT}/out/msm-kernel-autogvm-${KERNEL_OUT_VARIANT}defconfig/dist/autogvm_${KERNEL_BUILD_VARIANT}defconfig_kernel-uapi-headers.tar.gz ${B}/headers/kernel-uapi-headers.tar.gz
    cd ${B}/headers
    tar -xvzf kernel-uapi-headers.tar.gz
    rm -f kernel-uapi-headers.tar.gz
}

addtask do_populate_kernel_header_artifacts after do_compile before do_install

do_install () {
    cd ${B}
    headerdir=${B}/headers
    kerneldir=${D}${includedir}/linux-msm
    install -d $kerneldir

    if [ -d $headerdir/${includedir} ]; then
        cp -fR $headerdir/${includedir}/* $kerneldir/
    fi
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

ALLOW_EMPTY:${PN} = "1"

FILES:${PN}-dev += "linux-msm/*"

INHIBIT_DEFAULT_DEPS = "1"
