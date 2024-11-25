
SUMMARY = "multimedia framework"
DESCRIPTION = "packages for multimedia"
LICENSE = "GPLv2+ & LGPLv2+"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

ALLOW_EMPTY:${PN} = "1"
PACKAGES = "${PN}"

RDEPENDS:${PN} = "\
        videodlkm \
        gstreamer1.0 \
        gstreamer1.0-plugins-base \
        gstreamer1.0-plugins-good \
        gstreamer1.0-plugins-bad \
        gstreamer1.0-plugins-ugly \
        gstreamer1.0-libav \
        gdk-pixbuf-loader-bmp \
        gdk-pixbuf-loader-gif \
        ${@bb.utils.contains('DISTRO_FEATURES', 'qti-gstqeavb', 'gstreamer1.0-plugins-qeavb', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 'qti-omx', 'gstreamer1.0-omx mm-vdec-omx-test-lite mm-venc-omx-test-lite', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 'qti-codec2', 'codec2 gstreamer1.0-plugins-codec2 gstreamer1.0-plugins-vesdeliver gstreamer1.0-plugins-drmdecryptor drm-player-example', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 'qti-gstdeinterlace', 'gstreamer1.0-plugins-qvdeinterlace', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 'qti-gstqvrate', 'gstreamer1.0-plugins-qvrate', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 'qti-gstqvais', 'gstreamer1.0-plugin-qvais', '', d)} \
"

RDEPENDS:${PN}:remove:sa8797 = "\
        videodlkm \
"

# need codec2-app for Lemans targets since codec2-service not enabled yet on Lemans
RDEPENDS:${PN}:append:quin-gvm-lemans = " codec2-app"
RDEPENDS:${PN}:append:quin-gvm-lemans-dpk = " codec2-app"
RDEPENDS:${PN}:append:quin-gvm-monaco = " codec2-app"
RDEPENDS:${PN}:append:quin-gvm-monaco-dpk = " codec2-app"
