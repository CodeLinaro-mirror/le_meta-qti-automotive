FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://0001-wayland-Add-debug-log-to-identify-root-cause-of-fail.patch \
"

