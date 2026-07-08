SUMMARY = "QTI package group for weston"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-display \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    ${@bb.utils.contains_any("PREFERRED_VERSION_linux-msm", "5.15 6.1 6.12", "displaydlkm", "", d)} \
    libdrm \
    wayland \
    ${@bb.utils.contains("MACHINE_FEATURES", "qti-gvm", "wayland-utils", "", d)} \
    wayland-ivi-extension \
    weston \
    weston-init \
    weston-examples \
    display-hal-linux \
    display-commonsys-intf-linux \
    weston-sdm-extension \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'kiumd-headers', '', d)} \
    "

RDEPENDS:${PN}:remove:gen5 = "display-commonsys-intf-linux"
RDEPENDS:${PN}:remove:qti-dpk = "wayland-ivi-extension"
RDEPENDS:${PN}:append:qti-dpk = " weston-udev"
RDEPENDS:${PN}:remove:qti-dpk = "weston-sdm-extension"
RDEPENDS:${PN}:remove:gvm-gen5 = "display-commonsys-intf-linux"
