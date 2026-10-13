SUMMARY = "Virtio vhost USB gadget Linux kernel driver headers."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=8afb6abdac9a14cb18a0d6c9c151e9b4"

require qcvirtio-vhost-usb-gadget-driver-linux_git.inc
inherit module-headers

do_install() {
    install -d ${D}/${includedir}/linux/
    install -m 644 ${S}/include/uapi/linux/vhost_gadget.h ${D}/${includedir}/linux/
}

