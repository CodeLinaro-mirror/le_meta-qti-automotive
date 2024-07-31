inherit qsigning

PR = "r1signed"

SOC_HW_VERSION = "0x60020000"
SOC_VERS = "0x6002"
SIGNING_VERSION = "_eccv3"

EXTRA_OEMAKE:append = " SIGNED_KERNEL=1"
EXTRA_OEMAKE:append = " USER_BUILD_VARIANT=0"
EXTRA_OEMAKE:append = " DEVICE_STATUS=DEFAULT_UNLOCK=true"
EXTRA_OEMAKE:append = " TARGET_ARCHITECTURE=${T_ARCH}"

do_deploy[prefuncs] = 'sign_abl'

FILES:${PN} = "/boot"

T_ARCH = "arm"
T_ARCH:aarch64 = "aarch64"
