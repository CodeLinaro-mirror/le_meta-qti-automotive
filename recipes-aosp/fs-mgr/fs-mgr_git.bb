SUMMARY = "Android library for fs-mgr"
DESCRIPTION = "fs-mgr provides an interface for filesystem management. \
The fs-mgr interface allows for querying the filesystem, mounting and \
unmounting, and other functionality."
HOMEPAGE = "https://www.codelinaro.org/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += "logwrapper libmincrypt ext4-utils glib-2.0"

SRC_URI = "${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=system/core;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/system/core/fs_mgr"

inherit autotools pkgconfig

EXTRA_OECONF += "--with-glib"

BBCLASSEXTEND = "native"

PACKAGE_BEFORE_PN = "${PN}-utils"
FILES:${PN}-utils = "${bindir}/fs_mgr"
