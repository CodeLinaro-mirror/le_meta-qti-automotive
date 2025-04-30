SUMMARY = "Initramfs image for early-ramdisk-init"
LICENSE = "BSD-3-Clause-Clear"

DEPENDS += "mkbootimg-native virtual/kernel"
DEPENDS += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'audiolite-dlkm safelinux-system-cfg safelinux-sec-modules safelinux-cfg-modules', '', d)}"

IMAGE_CLASSES:remove = "qimage qimage-boot"

inherit image

IMAGE_FSTYPES = "cpio.lz4"

# avoid circular dependencies
EXTRA_IMAGEDEPENDS = ""
KERNELDEPMODDEPEND = ""

KERNEL_VERSION = "${@oe.utils.read_file('${STAGING_KERNEL_BUILDDIR}/kernel-abiversion')}"

EARLY_RAMDISK_KERNEL_MODULES ?= ""

# We really need just kexecboot, kexec and ubiattach
IMAGE_INSTALL = "\
    early-ramdisk-init libgcc kmod util-linux-libblkid \
    ${EARLY_RAMDISK_KERNEL_MODULES} \
"

python do_rootfs:prepend() {
    import re

    if(d.getVar('KERNEL_RAMDISK_DLKMS') == 'True'):
        kver = d.getVar('KERNEL_VERSION')

        f = open(d.getVar('STAGING_KERNEL_DIR') + "/modules.list.msm." + d.getVar('KERNEL_ARCH'))
        modules = f.readlines()
        f.close()

        for module in modules:
            tmp = module.replace("_", "-")
            kpack = tmp.split('.')[0]
            kpack = " kernel-module-" + kpack + "-" + kver
            d.appendVar('PACKAGE_INSTALL', kpack)
            bb.debug(1, "add install module: %s" % (kpack))
}

# Kernel images are installed as kernel module packages depenedencies
# Remove them from the initrd image
do_rootfs:append() {
    bb.build.exec_func('do_image_clean', d)
}

fakeroot do_image_clean() {
   rm -rf ${IMAGE_ROOTFS}/boot/*
}

# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = ""

IMAGE_LINGUAS = ""

FEED_DEPLOYDIR_BASE_URI = ""
LDCONFIGDEPEND = ""
IMAGE_ROOTFS_EXTRA_SPACE = "0"

# disable runtime dependency on run-postinsts -> update-rc.d
ROOTFS_BOOTSTRAP_INSTALL = ""

