SUMMARY = "adbd-linux provides adbd-relay service"
DESCRIPTION = "adbd-linux is a fork of https://github.com/tonyho/adbd-linux \
which is a port of adb to Linux. This fork extends tonyho's \
adbd-linux with adbd-relay which can be used to proxy adb from one VM to another."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=c1a3ff0b97f199c7ebcfdd4d3fed238e"

# base utils
inherit  pkgconfig systemd

DEPENDS += "openssl libcap glib-2.0 systemd"

REPO_NAME = "coqos-adbd"
SRC_URI = "${PATH_TO_REPO}/${REPO_NAME}/.git;protocol=${PROTO};destsuffix=${REPO_NAME};usehead=1"
SRC_URI:append = " file://adbd-relay-vm2.service"
SRC_URI:append = " file://adbd-relay-vm3.service"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/${REPO_NAME}"

SYSTEMD_PACKAGES = "adbd-relay"
SYSTEMD_SERVICE:adbd-relay = "adbd-relay-vm2.service adbd-relay-vm3.service"

# Source files are not included in the packages
PACKAGE_DEBUG_SPLIT_STYLE = "debug-without-src"

PACKAGES = "\
    adbd-relay \
    adbd-relay-dbg \
"

FILES:adbd-relay += "\
    ${sbindir}/adbd-relay \
    ${systemd_system_unitdir}/adbd-relay-vm2.service \
    ${systemd_system_unitdir}/adbd-relay-vm3.service \
"

FILES:adbd-relay-dbg += "\
    ${sbindir}/.debug/adbd-relay \
"

TARGET_CC_ARCH += "${LDFLAGS}"

do_configure() {
    ./configure ${EXTRA_OECONF}
}

do_compile() {
    oe_runmake adb/adbd
}

do_install() {
    oe_runmake install 'DESTDIR=${D}'

    # There is no separate install target for adbd-relay.
    # Remove all unwanted byproducts of the build/install process.
    rm ${D}${sbindir}/xdg-adbd
    rm ${D}${sbindir}/adbd
    # adb-usb2tcp is a host tool
    rm ${D}${sbindir}/adb-usb2tcp

    # Avoid installation of adbd systemd service file since the version provided
    # by adbd-linux repository can not satisfy all possible use cases.
    rm -r ${D}/usr/etc/systemd
    rmdir ${D}/usr/etc

    # install locally provided systemd unit file
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/adbd-relay-vm2.service ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/adbd-relay-vm3.service ${D}${systemd_system_unitdir}
}
