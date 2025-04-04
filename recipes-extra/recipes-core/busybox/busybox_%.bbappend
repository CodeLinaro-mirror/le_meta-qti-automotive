FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Only add selinux config if selinux feature is enabled
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'libselinux', '', d)}"
SRC_URI:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'file://selinux.cfg', '', d)}"
