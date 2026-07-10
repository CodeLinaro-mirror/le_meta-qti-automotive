SUMMARY = "Virtio vhost USB gadget Linux kernel driver."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "virtual/kernel"

require qcvirtio-vhost-usb-gadget-driver-linux_git.inc
inherit module

SRC_URI:append = " file://vhost_usb_gadget.rules"

KERNEL_MODULE_AUTOLOAD = "vhost_usb_gadget"

# Avoid adding prefix, that will let us to include coqoshv.rules in the same package
KERNEL_MODULE_PACKAGE_SUFFIX = ""

EXTRA_OEMAKE += "KDIR=${STAGING_KERNEL_DIR}"
MODULES_INSTALL_TARGET = ""
MAKE_TARGETS = ""

do_install:append() {
    # Copy udev rule
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/vhost_usb_gadget.rules ${D}${sysconfdir}/udev/rules.d

    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
    install -m 0644 ${S}/vhost_usb_gadget.ko ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
}

FILES:${PN}:append = " \
    ${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/vhost_usb_gadget.ko \
    /etc/udev/rules.d/vhost_usb_gadget.rules \
"

RPROVIDES:${PN} = "qcvirtio-vhost-usb-gadget-driver-linux-${KERNEL_VERSION}"
