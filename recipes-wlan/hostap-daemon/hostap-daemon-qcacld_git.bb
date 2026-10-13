SUMMARY = "hostapd for SoftAP"
DESCRIPTION = "Hostapd (host access point daemon) is a user space daemon \
               software enabling a network interface card to act as an \
               access point and authentication serve. And user application \
               can directly use this binary to start all kinds of Access Points.\
               For example, setup one Access Point with open mode(without password), \
               or with security mode(with password, like wpa2/wpa3). And the station \
               device can connect to this Access Point to get some service. \
               "
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/external/wpa_supplicant_8/COPYING;md5=5ebcb90236d1ad640558c3d3cd3035df"

DEPENDS += "libnl openssl pkgconfig"
SRC_URI = "${PATH_TO_REPO}/external/wpa_supplicant_8/.git;protocol=${PROTO};destsuffix=external/wpa_supplicant_8;usehead=1 \
           file://defconfig-qcacld"
SRCREV = "${AUTOREV}"
PR = "r4.2"

S = "${WORKDIR}/external/wpa_supplicant_8/hostapd"

inherit autotools-brokensep linux-kernel-base pkgconfig

do_configure() {
    install -m 0644 ${WORKDIR}/defconfig-qcacld .config
    echo "CFLAGS +=\"-I${STAGING_INCDIR}/libnl3\"" >> .config
}
do_install() {
    install -d ${D}${sysconfdir}
    install -m 0644 ${S}/hostapd.conf ${D}${sysconfdir}/hostapd.conf
    make install DESTDIR=${D} BINDIR=${sbindir}/
}
