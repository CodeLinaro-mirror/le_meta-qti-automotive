SUMMARY = "QTI package group for kgsl"
LICENSE = "BSD-3-Clause-Clear"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-graphics \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    ${@bb.utils.contains_any('PREFERRED_VERSION_linux-msm', '5.15 6.1', 'graphicsdlkm', '', d)} \
    ${@bb.utils.contains_any('MACHINE_FEATURES', 'qti-umd', 'ksyncdlkm', '', d)} \
    vulkan-loader \
    "
