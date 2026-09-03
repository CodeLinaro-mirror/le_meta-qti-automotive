FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://0001-Update-wayland-ivi-extension-to-be-aligned-with-west.patch \
    file://0001-ivi-controller-Use-weston_view_add_transform.patch \
    file://0001-wayland-ivi-extension-fix-weston-crash-when-surface-.patch \
"

do_install:append() {
    # Install ivi-wayland protocol XML files for gvm-touch-proxy and other clients
    install -d ${D}${datadir}/wayland-protocols

    install -m 0644 ${S}/protocol/ivi-input.xml ${D}${datadir}/wayland-protocols/ivi-input.xml
    install -m 0644 ${S}/protocol/ivi-wm.xml ${D}${datadir}/wayland-protocols/ivi-wm.xml
}

FILES:${PN} += "\
    ${datadir}/wayland-protocols/ivi-input.xml \
    ${datadir}/wayland-protocols/ivi-wm.xml \
"
