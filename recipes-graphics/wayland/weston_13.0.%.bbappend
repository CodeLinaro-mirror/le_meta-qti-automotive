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
            ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'owfds libuhab libkiumd bootkpi-logging', '', d)} \
"

REQUIRED_DISTRO_FEATURES:remove = "opengl"

FILESEXTRAPATHS:append := " :${THISDIR}/weston/"
CODE_DIR = "${@bb.utils.contains_any('PREFERRED_PROVIDER_virtual/kernel', 'linux-qcom-custom linux-qcom-custom-rt',"vendor/qcom/opensource/display/weston", "graphics/weston", d)}"
SRC_URI = "${PATH_TO_REPO}/${CODE_DIR}/.git;protocol=${PROTO};destsuffix=${CODE_DIR};usehead=1 \
           file://weston.png \
           file://weston.desktop \
           file://xwayland.weston-start \
           file://systemd-notify.weston-start \
"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/${CODE_DIR}"

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
