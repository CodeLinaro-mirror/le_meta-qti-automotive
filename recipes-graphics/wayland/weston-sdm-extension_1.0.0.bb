SUMMARY = "SDM extension for Weston"
DESCRIPTION = "Provides SDM extensions for Weston(a reference implementation of Wayland compositor), \
including sdm-backend, sdm-service and QTI contributed test cases, etc."
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause & MIT & Apache-2.0 & BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
                    file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
                    file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
                    file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

WESTON_MAJOR_VERSION = "13"

DEPENDS += "cairo \
            display-hal-headers display-hal-linux display-noship-linux \
            ${@bb.utils.contains_any('PREFERRED_PROVIDER_virtual/kernel', 'linux-qcom-custom linux-qcom-custom-rt', 'display-intf-headers', 'display-ship-linux', d)} \
            gbm gbm-headers \
            ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'libuhab', '', d)} \
            libinput \
            virtual/kernel-headers \
            pixman virtual/egl \
            systemd \
            wayland wayland-native wayland-protocols \
            weston \
            ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'bootkpi-logging power-utils powercyclemgr', '', d)} \
            ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'compute-resmgr', '', d)} \
"

DEPENDS:remove:sa8775 = "display-intf-headers"
DEPENDS:append:sa8775 = " display-ship-linux"
DEPENDS:remove:sa7255 = "display-intf-headers"
DEPENDS:append:sa7255 = " display-ship-linux"
CODE_DIR = "${@bb.utils.contains_any('PREFERRED_PROVIDER_virtual/kernel', 'linux-qcom-custom linux-qcom-custom-rt',"vendor/qcom/opensource/display/weston-sdm-extension", "${TARGET_DIR}graphics/weston-sdm-extension", d)}"
CODE_DIR:sa8775 = "${TARGET_DIR}graphics/weston-sdm-extension"
CODE_DIR:sa7255 = "${TARGET_DIR}graphics/weston-sdm-extension"

SRC_URI = "${PATH_TO_REPO}/${CODE_DIR}/.git;protocol=${PROTO};destsuffix=${CODE_DIR};usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/${CODE_DIR}"

inherit meson pkgconfig
#Introducing sleep-notify-service.bbclass for sleep-notify service
inherit ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'systemd sleep-notify-service', '', d)}

TARGET_CPPFLAGS += "-I${STAGING_INCDIR}/libdrm \
                    -I${STAGING_INCDIR}/qcom/display \
                    -I${STAGING_INCDIR}/sdm \
                    -I${STAGING_INCDIR}/sdm/core \
                    -I${STAGING_INCDIR}/libweston-${WESTON_MAJOR_VERSION} \
                    -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel} \
                    -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}/display \
"

# fix for uapi msm_drm.h header file related compilation issue
TARGET_CPPFLAGS += "-fno-operator-names"

PACKAGECONFIG ??= "${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'pmsnservice', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'early_init', 'early', '', d)} \
                   ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'rt_schedule', '', d)} \
"

# early-init
PACKAGECONFIG[early] = "-Denable-early-boot=true,-Denable-early-boot=false"
# pm
PACKAGECONFIG[pmsnservice] = "-Denable-pm-snservice=true,"
PACKAGECONFIG[pmdbus] = "-Denable-pm-dbus=true,"

do_install:append() {
    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'true', 'false', d)}; then
        install -d ${D}${systemd_system_unitdir}/
        install -m 0644 ${S}/sdm-backend/snservice_conf/sleep-notify@weston.service.d/weston.conf -D ${D}${systemd_system_unitdir}/sleep-notify@weston.service.d/weston.conf
    fi
}

SYSTEMD_SERVICE:${PN} = "${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'sleep-notify@weston.service', '', d)}"

# rt_schedule
PACKAGECONFIG[rt_schedule] = "-Denable-rt_schedule=true,"

FILES:${PN} += "\
    ${libdir}/libweston-${WESTON_MAJOR_VERSION}/* \
    ${libdir}/weston/* \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', '${systemd_system_unitdir}/*', '', d)} \
"
