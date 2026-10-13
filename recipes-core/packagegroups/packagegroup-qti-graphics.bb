SUMMARY = "QTI package group for kgsl"
LICENSE = "BSD-3-Clause-Clear"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-graphics \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    ${@bb.utils.contains_any('MACHINE_FEATURES', 'qti-umd', 'ksyncdlkm', '', d)} \
    vulkan-loader \
    ${@bb.utils.contains_any('MACHINE_FEATURES', 'qti-umd', 'auto-gfx-app', '', d)} \
    "

RDEPENDS:${PN}:gvm-gen4-5 += "graphics-hgsldlkm"
RDEPENDS:${PN}:gvm-gen5 += "graphics-hgsldlkm"
