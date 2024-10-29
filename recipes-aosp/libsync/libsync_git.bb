SUMMARY = "Android libsync"
DESCRIPTION = "Android libsync is a helper library which implements the ioctl of \
SYNC_IOC_FENCE_INFO and SW_SYNC_IOC_CREATE_FENCE for fence synchronization mechanism. \
Fence is used for synchronization of graphic buffer between CPU and GPU."
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=83eae6a29c0e876fdfebc0e44fb6fa2a"

DEPENDS += "glib-2.0 liblog"

SRC_URI = "\
    git://git.codelinaro.org/clo/la/platform/system/core;protocol=https;branch=android-framework.lnx.3.1.r8-rel;subpath=libsync; \
    file://0001-Build-libsync-with-autotool.patch \
    file://0001-Libsync-fix-freed-issue.patch \
"
SRCREV = "8fbe56b11ee7c1f8c87e9b71d89caa306c6cdebb"

S = "${WORKDIR}/libsync"

inherit autotools pkgconfig

EXTRA_OECONF = "--with-glib"
