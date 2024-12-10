FILESBBAPPENDPATH := "${THISDIR}"
FILESEXTRAPATHS =. "${FILESBBAPPENDPATH}/${BPN}-${PV}:${FILESBBAPPENDPATH}/${BPN}:"

SRC_URI:append = " ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', ' file://0001-DRM-front-end-display-DRM-front-end.patch', ' ', d)}"
SRC_URI:append = " ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', ' file://0002-DRM-FE-implement-of-drmGetPrimaryDeviceNameFromFd.patch', ' ', d)}"
EXTRA_OEMESON:append = " ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', ' -Denable_drm-fe=yes', ' ', d)}"

do_install:append () {
    install -d ${D}${includedir}
    install -m 644 ${S}/libdrm_macros.h ${D}${includedir}
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
