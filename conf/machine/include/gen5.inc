include conf/machine/include/basemachine.inc
include conf/machine/include/qclinux-bbmask.inc
include conf/distro/include/qclinux-bbmask-prop.inc
SOC_FAMILY = "gen5"
SOC_FAMILY ?= "sa-common"
include conf/machine/include/soc-family.inc
PRODUCT = "${MACHINE}"
PREFERRED_PROVIDER_virtual/kernel = "linux-qcom-custom-rt"
PREFERRED_PROVIDER_virtual/kernel-headers = "linux-kernel-qcom-headers"
PREFERRED_VERSION_linux-qcom-custom-rt = "6.6"

#grep use GPL-2.0-only version from meta-gplv2/recipes-extended/grep
PREFERRED_VERSION_grep = "2.5%"
PREFERRED_VERSION_libgpiod = "2.1.2"
preferred-kernel = "PREFERRED_VERSION_linux-qcom-custom-rt"
kernel-version = "6.6"
# Prefer gbm
PREFERRED_PROVIDER_virtual/libgbm = "gbm"

PREFERRED_VERSION_android-tools = "5.1.1.r37"
PREFERRED_VERSION_adreno = "0.1"
# Prefer adreno
PREFERRED_PROVIDER_virtual/wayland-egl  = "adreno"
PREFERRED_PROVIDER_virtual/egl          = "adreno"
PREFERRED_PROVIDER_virtual/libgles1     = "adreno"
PREFERRED_PROVIDER_virtual/libgles2     = "adreno"
VIRTUAL-RUNTIME_wayland-egl  = "adreno"
VIRTUAL-RUNTIME_egl          = "adreno"
VIRTUAL-RUNTIME_libgles1     = "adreno"
VIRTUAL-RUNTIME_libgles2     = "adreno"

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

EXTRA_IMAGEDEPENDS += "qcom-image-boot:do_sign_boot_img"

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
    qti-audio-ar \
    qti-data \
    qti-camera \
    qti-safety \
    qti-vnw \
    early-ramdisk-init \
    qti-security \
    qti-umd \
    qti-vmm \
    qti-compute-platform \
    qti-wlan \
    qti-wlan-hsp \
    qti-wlan-hmt \
"

MACHINE_FEATURES:remove = "${@bb.utils.contains('FACTORY_TEST', '1', 'qti-vmm', '', d)}"
SECTOOLS_VERSION = "sectools-v2"
SECTOOLS_SECURITY_PROFILE = "nord_tz_security_profile.xml"

SECTOOLS_V2_MULTI_PROFILE_SIGNING ?= "1"
NORD_SECTOOLS_SECURITY_PROFILE = "nord_tz_security_profile.xml"
SECA_SECTOOLS_SECURITY_PROFILE = "seca_tz_security_profile.xml"
SECTOOLS_V2_SECURITY_PROFILES = "${NORD_SECTOOLS_SECURITY_PROFILE} ${SECA_SECTOOLS_SECURITY_PROFILE}"

MACHINE_FEATURES:append = " qti-qcnode"
