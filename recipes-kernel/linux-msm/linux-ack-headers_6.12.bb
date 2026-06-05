inherit kernel-arch pkgconfig multilib_header

SUMMARY = "CAF Linux Kernel Headers"
DESCRIPTION = "Installs MSM kernel headers required to build userspace. \
These headers are installed in ${includedir}/linux-msm path. \
A ${PREFERRED_PROVIDER_virtual/kernel} symlink is provided for recipes using \
-I\${STAGING_INCDIR}/\${PREFERRED_PROVIDER_virtual/kernel}."
HOMEPAGE = "https://git.codelinaro.org"

LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

DEPENDS += "rsync-native"

PROVIDES = "linux-msm-headers virtual/kernel-headers"

COMPATIBLE_MACHINE = "gvm-gen4-5|gvm-gen5"

SRC_URI = "\
    ${PATH_TO_REPO}/kernel/kernel-${PV}/kernel_platform/common/.git;protocol=${PROTO};name=common;destsuffix=kernel/kernel-${PV}/kernel_platform/common;usehead=1 \
    ${PATH_TO_REPO}/kernel/kernel-${PV}/kernel_platform/soc-repo/.git;protocol=${PROTO};name=socrepo;destsuffix=kernel/kernel-${PV}/kernel_platform/soc-repo;usehead=1 \
    "

SRCREV_common = "${AUTOREV}"
SRCREV_socrepo = "${AUTOREV}"
SRCREV_FORMAT = "common_socrepo"

S = "${WORKDIR}/kernel/kernel-${PV}/kernel_platform/common"
SOC_REPO_INCLUDE = "${WORKDIR}/kernel/kernel-${PV}/kernel_platform/soc-repo/include"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
    # Copy soc-repo headers into common/include before headers_install
    install -d ${S}/include
    find ${SOC_REPO_INCLUDE} -type f | while read f; do
        rel="${f#${SOC_REPO_INCLUDE}/}"
        install -D -m 0644 "$f" "${S}/include/${rel}"
    done

    cd ${B}
    headerdir=${B}/headers
    kerneldir=${D}${includedir}/linux-msm
    install -d $kerneldir

    # Install all headers inside B and copy only required ones to D
    oe_runmake_call -C ${B} ARCH=${ARCH} headers_install O=$headerdir

    if [ -d $headerdir/include/generated ]; then
        install -d $kerneldir/include/generated/
        find $headerdir/include/generated -type f | while read f; do
            rel="${f#$headerdir/include/generated/}"
            install -D -m 0644 "$f" "$kerneldir/include/generated/${rel}"
        done
    fi

    if [ -d $headerdir/arch/${ARCH}/include/generated ]; then
        install -d $kerneldir/arch/${ARCH}/include/generated/
        find $headerdir/arch/${ARCH}/include/generated -type f | while read f; do
            rel="${f#$headerdir/arch/${ARCH}/include/generated/}"
            install -D -m 0644 "$f" "$kerneldir/arch/${ARCH}/include/generated/${rel}"
        done
    fi

    if [ -d $headerdir/${includedir} ]; then
        install -d $kerneldir
        find $headerdir/${includedir} -type f | while read f; do
            rel="${f#$headerdir/${includedir}/}"
            install -D -m 0644 "$f" "$kerneldir/${rel}"
        done
    fi

    # Remove ..install.cmd and .install
    find $kerneldir -name ..install.cmd | xargs rm -f
    find $kerneldir -name .install | xargs rm -f

    # Create symlink ${PN%-headers} -> linux-msm so recipes using
    # -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel} find headers.
    pn="${PN}"
    provider="${pn%-headers}"
    ln -sfn linux-msm ${D}${includedir}/${provider}
}

# kernel headers are generally machine specific
PACKAGE_ARCH = "${MACHINE_ARCH}"

# Allow to build empty main package, to include -dev package into the SDK
ALLOW_EMPTY:${PN} = "1"

FILES:${PN}-dev += "linux-msm/* ${PREFERRED_PROVIDER_virtual/kernel}"

INHIBIT_DEFAULT_DEPS = "1"
