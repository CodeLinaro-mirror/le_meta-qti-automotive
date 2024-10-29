SUMMARY = "cntvct log to calculate cntvct offset"

DESCRIPTION = "cntvct is an ARM counter that is started at power-on. \
Provides a mechanism to calculate the offset between the cntvct time and the \
monotonic time seen in journalctl output."

HOMEPAGE = "https://gitlab.com/CentOS/automotive/src"
LICENSE = "Apache-2.0 & LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE;md5=1c2e0cc0dec0b709fe547806b55737b0 \
                    file://${COMMON_LICENSE_DIR}/LGPL-2.1-or-later;md5=2a4f4fd2128ea2f65047ee63fbca9f68"

SRC_URI = "${CLO_LE_GIT}/platform/external/boot-time-analysis-tools;protocol=https;branch=main;destsuffix=git"
SRCREV = "9943935af12c09fb93228a087748d87151c3a9b5"
S = "${WORKDIR}/git/cntvct-log"

inherit meson systemd

SYSTEMD_SERVICE:${PN} = "cntvct@local-fs.service"

do_install:append() {
    install -D -m 0644 ${S}/usr/lib/systemd/system/cntvct@.service ${D}${systemd_system_unitdir}/cntvct@.service
}

FILES:${PN} += "${systemd_system_unitdir}/cntvct@.service"
