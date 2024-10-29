DESCRIPTION = "Android IPC utilities"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"
DEPENDS = "binder liblog libcutils libhardware libhardware-headers glib-2.0 system-core-headers libsync"
PR = "r1"

SRC_URI = "${PATH_TO_REPO}/frameworks/.git;protocol=${PROTO};destsuffix=frameworks;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/frameworks/libui"

inherit autotools pkgconfig

EXTRA_OECONF = "--with-glib"

CFLAGS += "-I${STAGING_INCDIR}/libselinux"

CPPFLAGS += "-fpermissive"

LDFLAGS += "-llog"
