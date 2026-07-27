FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://0001-Update-wayland-ivi-extension-to-be-aligned-with-west.patch \
    file://0001-ivi-controller-Use-weston_view_add_transform.patch \
    file://0001-wayland-ivi-extension-fix-weston-crash-when-surface-.patch \
"

