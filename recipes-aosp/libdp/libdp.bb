SUMMARY = "Library for Android dynamic partition "
DESCRIPTION = "Android dynamic partitions are a userspace partitioning system for Android. \
Super image is generated to repace system and vendor image when dynamic partitons are enabled. \
This library is used to parse the meta data in super image and create logic partitions to \
support launch Androind container in Linux"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=c1a3ff0b97f199c7ebcfdd4d3fed238e"

DEPENDS += "openssl gtest libsparse ext4-utils"

SRC_URI = "\
    git://git.codelinaro.org/clo/la/platform/system/core;protocol=https;nobranch=1; \
    file://0001-libdp-add-support-for-autoconf-build.patch \
    file://0001-libdp-Android11-ota-lib-porting-libbase_r.patch \
    file://0002-libdp-Android11-ota-lib-porting-liblp.patch \
    file://0003-libdp-Android11-ota-lib-porting-libdm.patch \
    file://0004-libdp-Android11-ota-lib-porting-fs_mgr.patch \
    file://0001-libdp-Create-symlink-for-dynamic-partitions.patch \
    file://0001-libdp-libdm-add-uuid-link-for-partions.patch \
    file://0001-libdp-update-fs_mgr-to-work-in-LV.patch \
"
SRCREV = "f9a075a9078eaebee234fb9be2f043613fe63da8"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

PACKAGES =+ "${PN}-test"
FILES_${PN}-test += "${bindir}/lp_test \
                     ${bindir}/dm_test \
                    "
