SUMMARY = "Android Sparse library"
DESCRIPTION = "Libparse is a library in common use by the various Android core host applications. \
It provides utilities to convert from raw to sparse images and back."
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += "zlib"

SRC_URI = "${CLO_LA_GIT}/platform/system/core;name=core;protocol=https;branch=aosp-new/sdk-release;destsuffix=git \
    file://0001-libsparse-Add-makefile-support-for-aosp-libsparse.patch \
    file://0001-Revert-libsparse-Fix-allocation-failures-on-32-bit-s.patch \
    file://0002-libsparse-Backport-Werror-programing-rule-issue.patch \
"
SRCREV = "db5a18a5fac8ac58177e87fc5e7e0033aa514aef"

S = "${WORKDIR}/git/libsparse"

inherit autotools pkgconfig

EXTRA_OECONF:append:class-native = " --enable-img-convert-utils"

BBCLASSEXTEND = "native"
