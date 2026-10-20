SUMMARY = "implement the EAVB FE virtual io driver"
DESCRIPTION = "The ethernet audio and video bridge frontend driver based on virtual io"
HOMEPAGE = "https://git.codelinaro.org/"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "virtual/kernel"
do_configure[depends] += "virtual/kernel:do_shared_workdir"

SRC_URI = "\
    ${PATH_TO_REPO}/vendor/qcom/opensource/eavb/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/eavb;usehead=1 \
    file://virtio-eavb_load.conf \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/eavb/eavb-fe/kernel/driver/virtio-eavb"

inherit module module-sign kernel-arch qperf qti-kernel-arch-clang
INHIBIT_PACKAGE_STRIP = "1"
EXTRA_OEMAKE += "CONFIG_ARCH_MSM=y"

do_install:append() {
    install -m 0755 ${WORKDIR}/virtio-eavb_load.conf -D ${D}${sysconfdir}/modules-load.d/virtio-eavb_load.conf
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILES:${PN} += "\
    ${sysconfdir}/* \
    ${nonarch_base_libdir}/modules/${KERNEL_VERSION}/* \
"

RPROVIDES:${PN} += "kernel-module-eavb-fe-${KERNEL_VERSION}"

# lock to avoid parallel compiling with techpack
do_compile[lockfiles] += "${TMPDIR}/qti-techpack.lock"