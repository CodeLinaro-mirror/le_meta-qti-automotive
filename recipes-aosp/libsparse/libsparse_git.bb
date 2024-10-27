SUMMARY = "Android Sparse library"
DESCRIPTION = "Libparse is a library in common use by the various Android core host applications. \
It provides utilities to convert from raw to sparse images and back."
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://../NOTICE;md5=c1a3ff0b97f199c7ebcfdd4d3fed238e"

DEPENDS += "zlib"

FILESPATH =+ "${AUTOSOURCES}:"
SRC_URI     =  "file://system/core"

S = "${WORKDIR}/system/core/libsparse"
SRCREV = "${AUTOREV}"


inherit autotools pkgconfig

EXTRA_OECONF:append:class-native = " --enable-img-convert-utils"

BBCLASSEXTEND = "native"
