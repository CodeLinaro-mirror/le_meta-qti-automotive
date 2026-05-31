SUMMARY = "Interface between VIRTIO device implementation and Linux device RTC"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "virtual/kernel"

require qcvirtio-rtc-host-driver-linux.inc

inherit module
KERNEL_MODULE_AUTOLOAD = "virtio_rtc_host"

KERNEL_MODULE_PACKAGE_SUFFIX = ""

EXTRA_OEMAKE += "KDIR=${STAGING_KERNEL_DIR}"
MODULES_INSTALL_TARGET = ""
MAKE_TARGETS = ""

do_install:append() {
    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
    install -m 0644 ${S}/virtio_rtc_host.ko ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
}

FILES:${PN} += "\
    ${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/virtio_rtc_host.ko \
"

RPROVIDES:${PN} = "kernel-module-rtc-host-${KERNEL_VERSION}"
