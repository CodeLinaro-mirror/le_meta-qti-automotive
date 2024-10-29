SUMMARY = "Safe integer operation library for C"
DESCRIPTION = "This library supplies a set of standard functions for performing and checking safe integer operations"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://NOTICE;md5=e7235a4d576addf0c399983b1c7f673e"

SRC_URI = "${PATH_TO_REPO}/external/safe-iop/.git;protocol=${PROTO};destsuffix=external/safe-iop;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/external/safe-iop"

inherit autotools-brokensep
