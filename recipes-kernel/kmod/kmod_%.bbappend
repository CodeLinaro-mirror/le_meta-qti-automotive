FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " file://blacklist.conf"

do_install:append () {
    install -Dm644 "${WORKDIR}/blacklist.conf" "${D}${sysconfdir}/modprobe.d/blacklist.conf"
}
