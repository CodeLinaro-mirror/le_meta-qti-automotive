SUMMARY = "Sos collects Linux distro data for analysis and debugging"
DESCRIPTION = "Sos is an extensible, portable, support data collection tool \
primarily aimed at Linux distributions and other UNIX-like operating systems."

HOMEPAGE = "https://github.com/sosreport/sos"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "${CLO_LE_GIT}/platform/external/sos;protocol=https;branch=main;destsuffix=git"
SRCREV = "77e4bdff5ee01355ebcde7759524034aafd65b18"
S = "${WORKDIR}/git"

inherit setuptools3

do_install:append() {
    install -D -m 644 ${S}/tmpfiles/tmpfilesd-sos-rh.conf ${D}${sysconfdir}/tmpfiles.d/sos-ignore.conf
    install -D -m 644 ${S}/${PN}.conf ${D}${sysconfdir}/${PN}/${PN}.conf

    rm -rf ${D}/usr/config
}

# See sos's setup.py install_requires for dependencies.
# -debugger provides pdb which is imported in sos python scripts
RDEPENDS:${PN} = "\
    ${PYTHON_PN}-debugger \
    ${PYTHON_PN}-pexpect \
"
