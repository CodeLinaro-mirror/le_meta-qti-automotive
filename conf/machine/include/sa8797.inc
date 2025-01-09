include conf/machine/include/basemachine.inc
include conf/machine/include/qclinux-bbmask.inc
SOC_FAMILY = "sa8797"
SOC_FAMILY ?= "sa-common"
include conf/machine/include/soc-family.inc
PRODUCT = "${MACHINE}"
PREFERRED_PROVIDER_virtual/kernel = "linux-qcom-custom-rt"
PREFERRED_PROVIDER_virtual/kernel-headers = "linux-kernel-qcom-headers"
PREFERRED_VERSION_linux-qcom-custom-rt = "6.6"

# Prefer Wayland/Weston version
PREFERRED_VERSION_weston = "8.0.0"
PREFERRED_VERSION_wayland = "1.18.0"
PREFERRED_VERSION_wayland-native = "1.18.0"
PREFERRED_VERSION_wayland-protocols = "1.20"
PREFERRED_VERSION_wayland-ivi-extension = "2.2.0"
preferred-kernel = "PREFERRED_VERSION_linux-qcom-custom-rt"
kernel-version = "6.6"
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
    qti-bluetooth \
    qti-core \
    qti-tools \
    qti-multimedia \
    qti-cv \
    qti-display \
    qti-graphics \
    qti-audio-aw \
    qti-data \
    qti-camera \
    qti-safety \
    qti-vnw \
    early-ramdisk-init \
    qti-security \
    qti-umd \
    qti-vmm \
"
MACHINE_FEATURES:remove = "\
    qti-safety \
    early-ramdisk-init \
"

SECTOOLS_VERSION = "sectools-v2"
SECTOOLS_V2_DIR = "${BSPDIR}/vendor/qcom/proprietary/sectools/Linux"
SECTOOLS_SECURITY_PROFILE = "${BSPDIR}/security/securemsm/security_profiles/nord_tz_security_profile.xml"
