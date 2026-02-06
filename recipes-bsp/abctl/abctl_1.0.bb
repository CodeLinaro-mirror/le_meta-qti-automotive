SUMMARY = "abctl"
DESCRIPTION = "abctl library and utility."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=b796c0007db682166a1721da80267bb2"

DEPENDS += "libgpt virtual/kernel-headers"
PR = "r1"

SRC_URI = "${PATH_TO_REPO}/abctl/.git;protocol=${PROTO};destsuffix=abctl;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/abctl/abctl"

inherit autotools systemd useradd

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "ab-updater"
USERADD_PARAM:${PN} = "--no-create-home -g ab-updater --shell /bin/false ab-updater"

SYSTEMD_SERVICE:${PN} = "ab-updater.service"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/ab-updater.service -D ${D}${systemd_system_unitdir}/ab-updater.service
}

EXTRA_OECONF = "--with-sanitized-headers=${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}"

CXXFLAGS:sa8775 = "-DSUPPORT_ENABLE_LV_ATOMIC_AB"
CXXFLAGS:sa7255 = "-DSUPPORT_ENABLE_LV_ATOMIC_AB"

CXXFLAGS:gen5 = "-DSUPPORT_ENABLE_LV_ATOMIC_AB -DRECOVERYINFO_PARTITION -DRECOVERYINFO_V1"

