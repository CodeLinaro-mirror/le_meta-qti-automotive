SUMMARY = "VMM GVM info utility"
DESCRIPTION = "Generate GvmInfo image for Gunyah VMs"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/vm-tools/vm-gvminfo/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/vm-tools/vm-gvminfo;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/vendor/qcom/opensource/vm-tools/vm-gvminfo"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit systemd

SYSTEMD_SERVICE:${PN} = "mkgvminfo-la.service"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/mkgvminfo ${D}${bindir}/mkgvminfo

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/mkgvminfo-la.service ${D}${systemd_unitdir}/system/mkgvminfo-la.service
}

FILES:${PN} += "${systemd_unitdir}/system/mkgvminfo-la.service"
