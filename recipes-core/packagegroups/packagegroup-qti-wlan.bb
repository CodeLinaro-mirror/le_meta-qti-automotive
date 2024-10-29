SUMMARY = "QTI package group for wlan"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-wlan \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    rfkill \
    hostap-daemon-qcacld \
    wireless-tools \
    iw \
    iputils-ping \
    iputils-arping \
    wlan-platform-dlkm\
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-wlan-rome', 'qcacld32-ll-rome', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-wlan-genoa', 'qcacld32-ll-genoa', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-wlan-hasting', 'qcacld32-ll-hasting-cnss0', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-wlan-hsp', 'qcacld32-ll-hsp', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-wlan-hmt', 'qcacld32-ll-hmt', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-dual-wlan', 'qcacld32-ll-hasting-cnss2', '', d)} \
    wlan-sigma-dut \
    wpa-supplicant \
    wlan-conf \
    cnss-wlan-load \
    "

RDEPENDS:${PN}:append:quin-gvm-gen4-2 = " hs20-osu-client dhcpcd"
