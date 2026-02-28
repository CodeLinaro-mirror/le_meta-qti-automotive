DEPENDS += "gbm gbm-headers \
            display-commonsys-intf-linux \
            ${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '5.4', 'libion', '', d)} \
            libsync \
            ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'libuhab', '', d)} \
            libcutils \
            virtual/kernel-headers \
            weston-sdm-extension-headers \
            ${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '5.15', 'libdmabufheap', '', d)} \
            ${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '6.1', 'libdmabufheap', '', d)} \
            ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'openwfd-client libuhab libkiumd bootkpi-logging', '', d)} \
"

REQUIRED_DISTRO_FEATURES:remove = "opengl"

FILESEXTRAPATHS:append := " :${THISDIR}/weston/"
SRC_URI += "file://0001-Weston-support-SDM-backend-on-weston-13.0.1.patch \
            file://0002-pixel-formats-Add-QC-specific-format-TP10_UBWC.patch \
            file://0003-compositor-Add-interface-to-load-gbm-buffer-backend.patch \
            file://0004-gl-renderer-add-support-for-rendering-protected-cont.patch \
            file://0005-gl-renderer-Refine-logic-of-drawing-overlay-view.patch \
            file://0006-backend-change-default-repaint-window-value-to-15.patch \
            file://0007-weston-add-atrace-marker-for-bebug.patch \
            file://0008-weston-porting-UMD-specific-changes-from-weston-10.patch \
            file://0009-gl-renderer-Make-YUV-format-choose-EXTERNAL_OES-text.patch \
            file://0010-weston-enable-ASAN-and-fix-odr-violation-error.patch \
"

UPSTREAM_CHECK_URI:remove = "https://wayland.freedesktop.org/releases.html"

RRECOMMENDS_${PN}:remove = "weston-init"

do_install:append() {
    install -d ${D}${datadir}/weston
    mv ${D}${libdir}/libweston-${WESTON_MAJOR_VERSION}/drm-backend.so ${D}${datadir}/weston/drm-backend.so
}

FILES:${PN}-dev = "${includedir} \
                ${libdir}/pkgconfig ${datadir}/pkgconfig \
                ${libdir}/${BPN}/libexec_weston.so \
                ${libdir}/libweston-13.so \
                "
# Some libraries on which sdm-backend depends
FILES:libweston-${WESTON_MAJOR_VERSION} += "${libdir}/libsession-helper.so \
                                            ${libdir}/liblibinput-backend.so \
                                            ${libdir}/libbacklight.so"
