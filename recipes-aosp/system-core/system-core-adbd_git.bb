SUMMARY = "Android debug bridge daemon"
DESCRIPTION = "Android debug bridge daemon runs on device, \
accepting commands from an adb server."
HOMEPAGE = "https://www.codelinaro.org/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += "libbase fs-mgr glib-2.0"

SRC_URI = "${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=system/core;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/system/core/adb"

inherit autotools pkgconfig systemd useradd

PACKAGECONFIG ?= "${@bb.utils.filter('DISTRO_FEATURES', 'selinux', d)}"
PACKAGECONFIG[selinux] = "--enable-selinux,--disable-selinux,libselinux"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "adb"

USERADD_PARAM:${PN} = "-g adb --no-create-home --shell /bin/false adb"

EXTRA_OECONF += "\
    --with-glib \
    --with-mkbootimg-includes=${WORKDIR}/system/core/mkbootimg/include/bootimg \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '', '--enable-adb-verity', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '--with-systemd', '', d)} \
"

FILES:${PN} += "\
    ${sbindir}/adbd \
    ${libdir}/libadbd.so.* \
    ${systemd_unitdir}/system/adbd.service \
    ${systemd_unitdir}/system/multi-user.target.wants/adbd.service \
    ${sysconfdir}/launch_adbd \
    ${sysconfdir}/initscripts/adbd \
    ${sysconfdir}/adb_devid \
"
