FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', ' file://sh_login.patch', '', d)} \
"
