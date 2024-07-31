SUMMARY = "Android library for mincrypt"
DESCRIPTION = "This library provides minimalistic encryption support and \
implements SHA1 and SHA-256 hash algoraithm"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://NOTICE;md5=c19179f3430fd533888100ab6616e114"

FILESPATH =+ "${AUTOSOURCES}:"
SRC_URI     =  "file://system/core"

S = "${WORKDIR}/system/core/libmincrypt"
SRCREV = "${AUTOREV}"


inherit autotools pkgconfig

EXTRA_OECONF += "--with-core-includes=${WORKDIR}/system/core/include"

BBCLASSEXTEND = "native"
