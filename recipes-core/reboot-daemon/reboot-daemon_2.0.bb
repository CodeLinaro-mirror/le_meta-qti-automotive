SUMMARY = "reboot-daemon"
DESCRIPTION = "Add reboot-daemon recipe for diag-reboot-app to support device reboot via QXDM"
HOMEPAGE = "http://git.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 "

SRC_URI = "\
    ${PATH_TO_REPO}/mdm-ss-mgr/reboot-daemon/.git;protocol=${PROTO};destsuffix=mdm-ss-mgr/reboot-daemon;usehead=1 \
    file://reboot-daemon.service \
"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/mdm-ss-mgr/reboot-daemon"

inherit autotools-brokensep systemd

SYSTEMD_SERVICE:${PN} = "reboot-daemon.service"

CFLAGS:append = " -D_GNU_SOURCE \
                  -DUBCORE"

do_install:append() {
    install -m 0755 ${S}/reboot-daemon -D ${D}/sbin/reboot-daemon
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${WORKDIR}/reboot-daemon.service -D ${D}${systemd_unitdir}/system/reboot-daemon.service
}
