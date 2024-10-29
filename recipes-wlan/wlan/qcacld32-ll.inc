PROVIDES_NAME = "kernel-module-${_MODNAME}"

DEPENDS += "wlan-platform-dlkm"
inherit autotools-brokensep module qperf module-sign qti-kernel-arch-clang

WLAN_PLATFORM_PATH = "${STAGING_INCDIR}/wlan-platform"
WLAN_KBUILD_EXTRA = "KBUILD_EXTRA_SYMBOLS=${WLAN_PLATFORM_PATH}/Module.symvers"
WLAN_PLATFORM_CFG = "\
                     CONFIG_CNSS_OUT_OF_TREE=y CONFIG_CNSS2=m \
                     CONFIG_CNSS2_QMI=y CONFIG_CNSS_QMI_SVC=m \
                     CONFIG_CNSS_PLAT_IPC_QMI_SVC=m \
                     CONFIG_CNSS_UTILS=m \
                     CONFIG_CNSS2_CONDITIONAL_POWEROFF=y \
                     WLAN_PLATFORM_INC=${WLAN_PLATFORM_PATH}/inc \
                     KBUILD_EXTRA="${WLAN_KBUILD_EXTRA}" \
                   "
EXTRA_OEMAKE:append = " ${WLAN_PLATFORM_CFG} "

_LINUX_BUILD_TOP = "${WORKDIR}/wlan"

do_unpack[deptask] = "do_populate_sysroot"
do_compile:prepend() {
    #Add gnu99 for compiler compatible issues
    sed -i '$a\ccflags-y += -std=gnu99' ${S}/Kbuild
    #In yocto system, get build tag by 'git log' in wlan src dir instead of 'git reflog' in work dir
    sed -i -e '/^ifeq ($(CONFIG_BUILD_TAG), y)/,/^endif/{/^ifeq ($(CONFIG_BUILD_TAG), y)/!{/^endif/!d}}' ${S}/Kbuild
    sed -i -e '/^ifeq ($(CONFIG_BUILD_TAG), y)/a\
    CLD_IDS = $(shell cd "$(WLAN_ROOT_LV)" && git log -1 | sed -nE '\''s/^\\s*Change-Id: (I[0-f]{10})[0-f]{30}\\s*\$\$/\\1/p'\'')\
    CMN_IDS = $(shell cd "$(WLAN_CMN_LV)" && git log -1 | sed -nE '\''s/^\\s*Change-Id: (I[0-f]{10})[0-f]{30}\\s*\$\$/\\1/p'\'')\
    TIMESTAMP = $(shell date -u +'%Y-%m-%dT%H:%M:%SZ')\
    BUILD_TAG = "$(TIMESTAMP); cld:$(CLD_IDS); cmn:$(CMN_IDS);"\
    CFLAGS_wlan_hdd_main.o += -DBUILD_TAG=\\"$(BUILD_TAG)\\"' ${S}/Kbuild
    CFG80211_FLAG="ccflags-y += -DCFG80211_SINGLE_NETDEV_MULTI_LINK_SUPPORT \
                   -DCFG80211_TX_CONTROL_PORT_LINK_SUPPORT \
                   -DCFG80211_11BE_BASIC \
                   -DCFG80211_EXTERNAL_AUTH_MLO_SUPPORT \
                   -DCFG80211_MULTI_AKM_CONNECT_SUPPORT"
    FRAME_SIZE_FLAG="ccflags-y += -Wframe-larger-than=4096"
    sed -i -e "/$(CONFIG_QCA_CLD_WLAN_PROFILE)_defconfig$/i${CFG80211_FLAG}" ${S}/Kbuild
    sed -i -e "/$(CONFIG_QCA_CLD_WLAN_PROFILE)_defconfig$/i${FRAME_SIZE_FLAG}" ${S}/Kbuild
}

RPROVIDES:${PN} += "${PROVIDES_NAME}-${KERNEL_VERSION}"

FILES:${PN} += "${nonarch_base_libdir}/firmware/wlan/*"
FILES:${PN} += "${nonarch_base_libdir}/firmware/*"
FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*"

LDFLAGS:aarch64 = "-O1 --hash-style=gnu --as-needed"

KERNEL_CC += "-Wno-error=maybe-uninitialized"
KERNEL_CC += "-Wno-error=stringop-overflow"
