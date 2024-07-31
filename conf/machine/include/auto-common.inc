include conf/machine/include/basemachine.inc

SOC_FAMILY ?= "sa-common"
SOC_FAMILY:prepend = "qcom:"
include conf/machine/include/soc-family.inc

# PREFERRED_PROVIDER_virtual/kernel ?= "linux-kernel-qcom"
# PREFERRED_PROVIDER_virtual/kernel-headers = "linux-msm-headers"

# TODO: transfer to use QCLninux nhlos bins provider
# Recipe provider for boot critical nhlos bins
# PREFERRED_PROVIDER_virtual/bootbins ?= "firmware-qcom-bootbins"

# TODO: transfer to use QCLninux disk partition configuratio provider
# Recipe provider for disk partition configuration
# PREFERRED_PROVIDER_virtual/partconf ?= "partition-confs"

# Enables timestamping functionality during building the kernel
KERNEL_DEBUG_TIMESTAMPS = "1"

SERIAL_CONSOLE ?= "${@['', '115200 ttyMSM0'][d.getVar('VARIANT', True) == ('' or 'debug')]}"
SERIAL_CONSOLES = "115200;ttyMSM0"

IMAGE_FSTYPES ?= "ext4"
PAGE_SIZE ?= '4096'
IMAGE_ROOTFS_ALIGNMENT = "4096"
EXTRA_IMAGECMD:ext4 += " -b 4096 "

IMAGE_CLASSES += "qimage"
