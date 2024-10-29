SUMMARY = "Android ext4-utils tools"
DESCRIPTION = "Command line tools to make sparse images from ext4 file system \
images and android images(.img) with ext4 file systems. This package contains \
tools like mkuserimg, ext4fixup and make_ext4fs tools."
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=bb2810bf31da2f6bb39e0bfa86091da3"

DEPENDS += "libcutils libpcre libsparse"

PR = "r1"

SRC_URI = "${PATH_TO_REPO}/system/extras/.git;protocol=${PROTO};destsuffix=system/extras;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/system/extras/ext4_utils"

inherit autotools pkgconfig

PACKAGECONFIG:class-target ?= "${@bb.utils.filter('DISTRO_FEATURES', 'selinux', d)}"
PACKAGECONFIG:class-native ?= "${@bb.utils.filter('DISTRO_FEATURES_NATIVE', 'selinux', d)}"
PACKAGECONFIG[selinux] = "--enable-selinux,--disable-selinux,libselinux"

CPPFLAGS:append = " -I${STAGING_INCDIR}/cutils"

BBCLASSEXTEND = "native"

MACHINEOVERRIDES:class-native = "${MACHINE}:${SOC_FAMILY}"
