SUMMARY = "Dynamic partition loader"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "libdp"

FILESEXTRAPATHS:prepend := "${THISDIR}:"
SRC_URI = "file://dploader"
S = "${WORKDIR}/dploader"

inherit autotools pkgconfig systemd

SYSTEMD_SERVICE:${PN} = "dploader.service"

do_install:append() {
       install -d ${D}${systemd_unitdir}/system/
       install -m 0644 ${WORKDIR}/dploader/dploader.service ${D}${systemd_unitdir}/system/dploader.service
}

RDEPENDS:${PN} += "libdp"
