SUMMARY = "SDM extension for Weston"
DESCRIPTION = "Provides SDM extensions for Weston(a reference implementation of Wayland compositor), \
including sdm-backend, sdm-service and QTI contributed test cases, etc."
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause & MIT & Apache-2.0 & BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
                    file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
                    file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
                    file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

WESTON_MAJOR_VERSION = "10"

DEPENDS += "cairo \
            dbus \
            display-hal-headers display-hal-linux display-noship-linux display-ship-linux \
            gbm gbm-headers \
            ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'libuhab', '', d)} \
            libinput \
            virtual/kernel-headers \
            pixman virtual/egl \
            systemd \
            wayland wayland-native wayland-protocols \
            weston \
            ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'bootkpi-logging', '', d)} \
"

SRC_URI = "${PATH_TO_REPO}/graphics/weston-sdm-extension/.git;protocol=${PROTO};destsuffix=graphics/weston-sdm-extension;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/graphics/weston-sdm-extension"

inherit meson pkgconfig

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

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'early_init', 'early', '', d)}"
# early-init
PACKAGECONFIG[early] = "-Denable-early-boot=true,-Denable-early-boot=false"

FILES:${PN} += "\
    ${libdir}/libweston-${WESTON_MAJOR_VERSION}/* \
    ${libdir}/weston/* \
"
