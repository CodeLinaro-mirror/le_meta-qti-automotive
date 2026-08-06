SUMMARY = "Client for Wi-Fi Protected Access (WPA)"
DESCRIPTION = "WPA supplicant client used for WLAN STA/AP/P2P/WPS such feature test"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/external/wpa_supplicant_8/COPYING;md5=5ebcb90236d1ad640558c3d3cd3035df"

DEPENDS += "dbus libnl openssl"
SRC_URI = "${PATH_TO_REPO}/external/wpa_supplicant_8/.git;protocol=${PROTO};destsuffix=external/wpa_supplicant_8;usehead=1 \
           file://wpa_supplicant.conf-sane \
           file://defconfig-qcacld \
           file://wpa-supplicant.sh \
           file://99_wpa_supplicant \
          "
SRCREV = "${AUTOREV}"
PR = "r5.2"

SOLIBS = "*.so"
FILES_SOLIBSDEV = ""

S = "${WORKDIR}/external/wpa_supplicant_8/wpa_supplicant"

inherit autotools-brokensep linux-kernel-base pkgconfig systemd

SYSTEMD_SERVICE:${PN} = "wpa_supplicant.service"
SYSTEMD_AUTO_ENABLE = "disable"

export BINDIR = "${sbindir}"

do_configure() {
    install -m 0644 ${WORKDIR}/defconfig-qcacld .config
    echo "CFLAGS +=\"-I${STAGING_INCDIR}/libnl3\"" >> .config
}
do_install() {
    make install DESTDIR=${D} BINDIR=${sbindir} LIBDIR=${libdir} INCDIR=${includedir}

    install -d ${D}${docdir}/wpa_supplicant
    install -m 644 ${S}/wpa_supplicant.conf ${D}${docdir}/wpa_supplicant

    install -d ${D}${sysconfdir}
    install -m 600 ${WORKDIR}/wpa_supplicant.conf-sane ${D}${sysconfdir}/wpa_supplicant.conf

    install -d ${D}${sysconfdir}/network/if-pre-up.d/
    install -d ${D}${sysconfdir}/network/if-post-down.d/
    install -d ${D}${sysconfdir}/network/if-down.d/
    install -m 755 ${WORKDIR}/wpa-supplicant.sh ${D}${sysconfdir}/network/if-pre-up.d/wpa-supplicant
    cd ${D}${sysconfdir}/network/ && \
    ln -sf ../if-pre-up.d/wpa-supplicant if-post-down.d/wpa-supplicant

    install -d ${D}/${sysconfdir}/dbus-1/system.d
    install -m 644 ${S}/dbus/dbus-wpa_supplicant.conf ${D}/${sysconfdir}/dbus-1/system.d
    install -d ${D}/${datadir}/dbus-1/system-services
    install -m 644 ${S}/dbus/*.service ${D}/${datadir}/dbus-1/system-services

    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -d ${D}/${systemd_unitdir}/system
        install -m 644 ${S}/systemd/*.service ${D}/${systemd_unitdir}/system
    fi

    install -d ${D}${sysconfdir}/default/volatiles
    install -m 0644 ${WORKDIR}/99_wpa_supplicant ${D}${sysconfdir}/default/volatiles
}

CONFFILES:${PN} += "${sysconfdir}/wpa_supplicant.conf"
FILES:${PN} += "${systemd_unitdir}/system/*"
FILES:${PN} += "${datadir}"
FILES:${PN} += "${datadir}/dbus-1/system-services/*"

