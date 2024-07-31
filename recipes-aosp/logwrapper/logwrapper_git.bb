SUMMARY = "logwrapper - Android wrapper library for logging"
DESCRIPTION = "This library provides a wrapper interface for logging to the Android logging system. \
Includes an option to log to the kernel log."
HOMEPAGE = "https://www.codelinaro.org/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += "libcutils liblog"

FILESPATH =+ "${AUTOSOURCES}:"
SRC_URI     =  "file://system/core"
S = "${WORKDIR}/system/core/logwrapper"
SRCREV = "${AUTOREV}"

inherit autotools pkgconfig

BBCLASSEXTEND = "native"

PACKAGE_BEFORE_PN = "${PN}-utils"
FILES:${PN}-utils = "${bindir}/logwrapper"
