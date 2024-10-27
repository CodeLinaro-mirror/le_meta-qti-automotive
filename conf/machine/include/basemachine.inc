BASEMACHINE ??= ""

# Add BASEMACHINE to machine overrides
MACHINEOVERRIDES =. "${@['', '${BASEMACHINE}:']['${BASEMACHINE}' != '']}"

# Define BASEMACHINE_ARCH
BASEMACHINE_ARCH = "${@['${MACHINE_ARCH}', '${BASEMACHINE}']['${BASEMACHINE}' != '']}"

# Add BASEMACHINE_ARCH to PACKAGE_ARCHS. 
# This variable is originally set in bitbake.conf
PACKAGE_ARCHS .= "${@['', ' ${BASEMACHINE_ARCH}']['${BASEMACHINE_ARCH}' != '${MACHINE_ARCH}']}"
PACKAGE_ARCHS[vardepsexclude] += "BASEMACHINE_ARCH"

# Default Image names
BOOTIMAGE_TARGET ?= "${PRODUCT}-boot.img"
CACHEIMAGE_TARGET ?= "${PRODUCT}-cache.ext4"
PERSISTIMAGE_TARGET ?= "${PRODUCT}-persist.ext4"
RECOVERYIMAGE_TARGET ?= "${PRODUCT}-recoveryfs.ext4"
SYSTEMIMAGE_TARGET ?= "${PRODUCT}-sysfs.ext4"
SYSTEMRWIMAGE_TARGET ?= "${PRODUCT}-systemrw.ext4"
USERDATAIMAGE_TARGET ?= "${PRODUCT}-usrfs.ext4"
VBMETAIMAGE_TARGET ?= "${PRODUCT}-vbmeta.img"
VENDORBOOTIMAGE_TARGET ?= "${PRODUCT}-vendor_boot.img"
BOOTINIT_TARGET ?= "${PRODUCT}-boot_init.img"

# Flag to Enable QPermissions
QPERMISSIONS_ENABLE ?= "0"
