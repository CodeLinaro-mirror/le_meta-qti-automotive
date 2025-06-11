#
# Tune Settings for oryon-1
#
DEFAULTTUNE ?= "oryon-1"

TUNEVALID[oryon-1] = "Enable oryon-1 specific processor optimizations"
TUNE_CCARGS .= "${@bb.utils.contains('TUNE_FEATURES', 'oryon-1', ' -mcpu=oryon-1', '', d)}"

require conf/machine/include/arm/arch-armv8-6a.inc

AVAILTUNES                                         += "oryon-1"
ARMPKGARCH:tune-oryon-1                             = "oryon-1"
TUNE_FEATURES:tune-oryon-1                          = "aarch64 oryon-1"
PACKAGE_EXTRA_ARCHS:tune-oryon-1                    = "${PACKAGE_EXTRA_ARCHS:tune-armv8-6a-crypto} oryon-1"
BASE_LIB:tune-oryon-1                               = "lib64"
