FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

BLACKLIST_FILE = "${@bb.utils.contains("MACHINE_FEATURES", "qti-gvm", "blacklist_gvm.conf", "blacklist.conf", d)}"
SRC_URI:append = " file://${BLACKLIST_FILE}"

do_install:append () {
    install -Dm644 "${WORKDIR}/${BLACKLIST_FILE}" "${D}${sysconfdir}/modprobe.d/blacklist.conf"
}
