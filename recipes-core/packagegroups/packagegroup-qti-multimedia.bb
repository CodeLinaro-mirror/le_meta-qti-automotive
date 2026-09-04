
SUMMARY = "multimedia framework"
DESCRIPTION = "packages for multimedia"
LICENSE = "GPL-2.0-or-later & LGPL-2.0-or-later"

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
        gstreamer1.0-plugins-qvconv \
        ${@bb.utils.contains('DISTRO_FEATURES', 'qti-omx', 'gstreamer1.0-omx mm-vdec-omx-test-lite mm-venc-omx-test-lite', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 'qti-codec2', 'codec2 gstreamer1.0-plugins-codec2 gstreamer1.0-plugins-vesdeliver gstreamer1.0-plugins-drmdecryptor drm-player-example', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 'qti-gstdeinterlace', 'gstreamer1.0-plugins-qvdeinterlace', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 'qti-gstqvrate', 'gstreamer1.0-plugins-qvrate', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 'qti-gstqvais', 'gstreamer1.0-plugin-qvais', '', d)} \
        ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'gstreamer1.0-plugins-qcarcamsrc gstreamer1.0-plugins-vidc', '', d)} \
        gst-qv-codec-test \
        gstreamer1.0-plugins-extpoolsink \
"

RDEPENDS:${PN}:remove:gen5 = "\
        videodlkm \
"

# Remove gstreamer components that are not required for qcvirtio-video
RDEPENDS:${PN}:remove = "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'qti-qcvirtio', 'gstreamer1.0-plugins-qvconv', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'qti-qcvirtio', 'gstreamer1.0-plugins-qcarcamsrc', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'qti-qcvirtio', 'gstreamer1.0-plugins-good', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'qti-qcvirtio', 'gstreamer1.0-plugins-extpoolsink', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'qti-qcvirtio', 'videodlkm', '', d)} \
"

RDEPENDS:${PN}:remove:gvm-gen4-5 = "\
        ${@bb.utils.contains('DISTRO_FEATURES', 'qti-codec2', 'gstreamer1.0-plugins-codec2', '', d)} \
"

RDEPENDS:${PN}:append:gvm-gen4-5 = " \
        ${@bb.utils.contains('DISTRO_FEATURES', 'qti-codec2', ' gstreamer1.0-plugins-codec2-lemans gstreamer1.0-plugins-codec2-monaco', '', d)} \
"

RDEPENDS:${PN}:remove:gvm-gen5 = "\
        ${@bb.utils.contains('DISTRO_FEATURES', 'qti-codec2', 'gstreamer1.0-plugins-codec2', '', d)} \
"

RDEPENDS:${PN}:append:gvm-gen5 = " \
        ${@bb.utils.contains('DISTRO_FEATURES', 'qti-codec2', ' gstreamer1.0-plugins-codec2-gen5', '', d)} \
"
