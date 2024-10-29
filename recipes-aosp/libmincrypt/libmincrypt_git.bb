SUMMARY = "Android library for mincrypt"
DESCRIPTION = "This library provides minimalistic encryption support and \
implements SHA1 and SHA-256 hash algoraithm"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://NOTICE;md5=c19179f3430fd533888100ab6616e114"

SRC_URI = "${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=system/core;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/system/core/libmincrypt"

inherit autotools pkgconfig

EXTRA_OECONF += "--with-core-includes=${WORKDIR}/system/core/include"

BBCLASSEXTEND = "native"
