include conf/machine/include/basemachine.inc
include conf/machine/include/qclinux-bbmask.inc

SOC_FAMILY = "sa8797"
SOC_FAMILY ?= "sa-common"
include conf/machine/include/soc-family.inc
PRODUCT = "${MACHINE}"
PREFERRED_PROVIDER_virtual/kernel = "linux-qcom-custom"
PREFERRED_VERSION_linux-qcom-custom = "6.6"
PREFERRED_PROVIDER_virtual/kernel-headers = "linux-kernel-qcom-headers"

preferred-kernel = "PREFERRED_VERSION_linux-qcom-custom"
kernel-version = "6.6"

# Enables timestamping functionality during building the kernel
KERNEL_DEBUG_TIMESTAMPS = "1"

SERIAL_CONSOLE ?= "${@['', '115200 ttyMSM0'][d.getVar('VARIANT', True) == ('' or 'debug')]}"
SERIAL_CONSOLES = "115200;ttyMSM0"

IMAGE_FSTYPES ?= "ext4"
PAGE_SIZE ?= '4096'
IMAGE_ROOTFS_ALIGNMENT = "4096"
EXTRA_IMAGECMD_ext4 += " -b 4096 "

IMAGE_CLASSES += "qimage"

IMAGE_INSTALL += "\
    openssh-scp \
    openssh-ssh \
    openssh-sshd \
    openssh-sftp \
    openssh-sftp-server \
    "

IMAGE_FEATURES += "ssh-server-openssh debug-tweaks"

EXTRA_IMAGEDEPENDS += "qcom-image-boot:do_sign_boot_img"

IMAGE_FEATURES:remove = " read-only-rootfs"

MACHINE_FEATURES:append = "\
    qti-core \
    qti-tools \
    ${@bb.utils.contains("ACTUAL_KERNEL", "linux-ark", "qti-gunyah", "", d)} \
    early-ramdisk-init \
"

SECTOOLS_VERSION = "sectools-v2"
SECTOOLS_SECURITY_PROFILE = "nord_tz_security_profile.xml"
SECTOOLS_V2_DIR = "${BSPDIR}/vendor/qcom/proprietary/sectools/Linux"
