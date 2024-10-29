include conf/machine/include/basemachine.inc
include conf/machine/include/qclinux-bbmask.inc
SOC_FAMILY = "sa8797"
SOC_FAMILY ?= "sa-common"
include conf/machine/include/soc-family.inc
PRODUCT = "${MACHINE}"
ACTUAL_KERNEL = "${@oe.utils.ifelse((os.path.exists(d.getVar('BSPDIR', True) + '/kernel/rh-fedora-kernel-6.9')), 'linux-ark', 'linux-qcom-custom')}"
PREFERRED_PROVIDER_virtual/kernel = "${ACTUAL_KERNEL}"
# PREFERRED_PROVIDER_virtual/kernel = "linux-qcom-custom"
PREFERRED_PROVIDER_virtual/kernel-headers = "linux-kernel-qcom-headers"
PREFERRED_VERSION_linux-qcom = "6.6"
PREFERRED_VERSION_linux-ark = "6.9"
# PREFERRED_VERSION_linux-ark = "6.7"

# Prefer Wayland/Weston version
PREFERRED_VERSION_weston = "8.0.0"
PREFERRED_VERSION_wayland = "1.18.0"
PREFERRED_VERSION_wayland-native = "1.18.0"
PREFERRED_VERSION_wayland-protocols = "1.20"
PREFERRED_VERSION_wayland-ivi-extension = "2.2.0"
preferred-kernel = "${@oe.utils.ifelse(d.getVar('PREFERRED_PROVIDER_virtual/kernel') == 'linux-ark', 'PREFERRED_VERSION_linux-ark', 'PREFERRED_VERSION_linux-qcom')}"
kernel-version = "${@oe.utils.ifelse(d.getVar('PREFERRED_PROVIDER_virtual/kernel') == 'linux-ark', '6.9', '6.6')}"
# Prefer gbm
PREFERRED_PROVIDER_virtual/libgbm = "gbm"

PREFERRED_VERSION_adreno = "0.1"
# Prefer adreno
PREFERRED_PROVIDER_virtual/wayland-egl  = "adreno"
PREFERRED_PROVIDER_virtual/egl          = "adreno"
PREFERRED_PROVIDER_virtual/libgles1     = "adreno"
PREFERRED_PROVIDER_virtual/libgles2     = "adreno"

BBMASK:sa8797 += "meta-qti-bsp/meta-qti-base/recipes-kernel/edk2/edk2_4.0.bb"
BBMASK:sa8797 += "meta-qti-bsp/meta-qti-base/recipes-kernel/edk2/edk2_5.0.bb"

# Enables timestamping functionality during building the kernel
KERNEL_DEBUG_TIMESTAMPS = "1"
# mesa provides these packags and others like virtual/libgl.
# This is to enable mesa, adreno and gbm work together, and pass
# yocot-check-layer script check.
BB_MULTI_PROVIDER_ALLOWED += "virtual/wayland-egl virtual/egl virtual/libgles1 virtual/libgles2 virtual/libgbm"

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
IMAGE_INSTALL:remove = "${@bb.utils.contains('DISTRO_FEATURES', 'qti-rumi', 'openssh-scp openssh-ssh openssh-sshd openssh-sftp openssh-sftp-server', '', d)}"


IMAGE_FEATURES += "ssh-server-openssh"

EXTRA_IMAGEDEPENDS += "qti-image-ark-boot:do_sign_boot_img"

IMAGE_FEATURES:append = " read-only-rootfs"

MACHINE_FEATURES += "\
    qti-core \
    qti-tools \
    qti-multimedia \
    qti-display \
    qti-audio \
    qti-data \
    ${@bb.utils.contains("ACTUAL_KERNEL", "linux-ark", "qti-vmm", "", d)} \
    qti-camera \
    qti-safety \
    qti-vnw \
    early-ramdisk-init \
    qti-security \
    qti-umd \
    ${@bb.utils.contains("ACTUAL_KERNEL", "linux-ark", "qti-gunyah", "", d)} \
"

MACHINE_FEATURES:remove = "\
    qti-display \
    qti-audio \
    qti-camera \
    qti-safety \
    qti-vnw \
    early-ramdisk-init \
    qti-vmm \
"

SECTOOLS_VERSION = "sectools-v2"
