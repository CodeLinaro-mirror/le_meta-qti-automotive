FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://0001-Update-wayland-ivi-extension-to-be-aligned-with-west.patch \
"

