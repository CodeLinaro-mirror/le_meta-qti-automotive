include conf/machine/include/basemachine.inc
include conf/machine/include/qclinux-bbmask.inc

SOC_FAMILY = "sa8775"
include conf/machine/include/soc-family.inc
PRODUCT = "${MACHINE}"

include conf/machine/include/arm/armv8-2a/tune-cortexa78c.inc
TUNE_FEATURES:tune-cortexa78c = "${TUNE_FEATURES:tune-armv8-2a-crypto} cortexa78c"

# Prefer Wayland/Weston version
PREFERRED_VERSION_weston = "8.0.0"
PREFERRED_VERSION_wayland = "1.18.0"
PREFERRED_VERSION_wayland-native = "1.18.0"
PREFERRED_VERSION_wayland-protocols = "1.20"
PREFERRED_VERSION_wayland-ivi-extension = "2.2.0"
# Prefer gbm
PREFERRED_PROVIDER_virtual/libgbm = "gbm"

PREFERRED_VERSION_android-tools = "5.1.1.r37"
PREFERRED_VERSION_adreno = "0.1"
# Prefer adreno
PREFERRED_PROVIDER_virtual/wayland-egl  = "adreno"
PREFERRED_PROVIDER_virtual/egl          = "adreno"
PREFERRED_PROVIDER_virtual/libgles1     = "adreno"
PREFERRED_PROVIDER_virtual/libgles2     = "adreno"

BBMASK += "meta-qti-bsp/meta-qti-base/recipes-wlan"

RUSTVERSION = "1.75%"

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

IMAGE_FEATURES += "ssh-server-openssh"

EXTRA_IMAGEDEPENDS += "qcom-image-boot:do_sign_boot_img"

IMAGE_FEATURES:append = " read-only-rootfs"

MACHINE_FEATURES += "\
    qti-core \
    qti-tools \
    qti-multimedia \
    qti-display \
    qti-graphics \
    qti-audio \
    qti-vmm \
    qti-camera \
    qti-safety \
    qti-data \
    qti-vnw \
    early-ramdisk-init \
    qti-security \
    qti-umd \
    qti-gunyah \
    qti-compute-platform \
"

MACHINE_FEATURES:remove = "\
    qti-multimedia \
    qti-display \
    qti-graphics \
    qti-audio \
    qti-vmm \
    qti-camera \
    qti-safety \
    qti-data \
    qti-vnw \
    early-ramdisk-init \
    qti-security \
    qti-umd \
    qti-gunyah \
    qti-compute-platform \
"

MACHINE_FEATURES:append = "\
    ${@bb.utils.contains('FACTORY_TEST', '1', 'qti-bluetooth', '', d)} \
"

MACHINE_FEATURES:remove = "\
    ${@bb.utils.contains('TCMODE', 'external-ubuntu', 'qti-vmm', '', d)} \
    ${@bb.utils.contains('FACTORY_TEST', '1', 'qti-vmm', '', d)} \
"

SECTOOLS_VERSION = "sectools-v2"
SECTOOLS_SECURITY_PROFILE = "lemans_tz_security_profile.xml"

EARLY_RAMDISK_KERNEL_MODULES = "\
    kernel-module-pinctrl-msm-${KERNEL_VERSION} \
    kernel-module-qcom-geni-se-${KERNEL_VERSION} \
    kernel-module-qcom-geni-serial-${KERNEL_VERSION} \
    kernel-module-qcom-ipcc-${KERNEL_VERSION} \
    kernel-module-phy-qcom-qmp-ufs-${KERNEL_VERSION} \
    kernel-module-ufs-qcom-${KERNEL_VERSION} \
    kernel-module-ufshcd-core-${KERNEL_VERSION} \
    kernel-module-ufshcd-pltfrm-${KERNEL_VERSION} \
    kernel-module-pinctrl-sa8775p-${KERNEL_VERSION} \
    kernel-module-aquantia-${KERNEL_VERSION}  \
    kernel-module-marvell-${KERNEL_VERSION} \
    kernel-module-virtio-mmio-${KERNEL_VERSION} \
    kernel-module-ipv6-${KERNEL_VERSION} \
    kernel-module-kiumd-${KERNEL_VERSION} \
    kernel-module-iommu-iova-map-${KERNEL_VERSION} \
    kernel-module-scm-user-intf-${KERNEL_VERSION} \
    kernel-module-vfio-platform-base-${KERNEL_VERSION} \
    kernel-module-vfio-platform-${KERNEL_VERSION} \
    kernel-module-kiumd-kgsl-${KERNEL_VERSION} \
    kernel-module-vfio-iommu-qcom-${KERNEL_VERSION} \
    kernel-module-ipcc-shmem-test-module-${KERNEL_VERSION} \
    kernel-module-i2c-qcom-geni-${KERNEL_VERSION} \
    kernel-module-qcom-scm-oot-${KERNEL_VERSION} \
    kernel-module-qtee-shmbridge-${KERNEL_VERSION} \
    kernel-module-qcom-smcinvoke-${KERNEL_VERSION} \
    kernel-module-spidev-${KERNEL_VERSION} \
    kernel-module-qcom-dload-mode-${KERNEL_VERSION} \
    kernel-module-qcom-uscmi-${KERNEL_VERSION} \
    kernel-module-mhi-ep-net-${KERNEL_VERSION} \
    kernel-module-kryo-arm64-edac-${KERNEL_VERSION} \
    kernel-module-socinfo-${KERNEL_VERSION} \
"
