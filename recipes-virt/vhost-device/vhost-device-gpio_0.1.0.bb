SUMMARY = "vhost gpio backend device"
DESCRIPTION = "A vhost-user backend that emulates a VirtIO GPIO device"
HOMEPAGE = "https://github.com/rust-vmm/vhost-device"
LICENSE = "Apache-2.0 | BSD-3-Clause"
LIC_FILES_CHKSUM = "\
    file://LICENSE-APACHE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
    file://LICENSE-BSD-3-Clause;md5=2489db1359f496fff34bd393df63947e \
"
DEPENDS += "libgpiod"
# libgpiod-sys generates bindings using bindgen, which depends on clang
DEPENDS += "clang-native"
SKIP_RECIPE[vhost-device-gpio] ?= "${@bb.utils.contains('BBFILE_COLLECTIONS', 'clang-layer', '', 'Depends on clang-native from meta-clang which is not included', d)}"

SRC_URI = "${PATH_TO_REPO}/external/vhost-device/.git;protocol=${PROTO};destsuffix=external/vhost-device;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/external/vhost-device"
CARGO_SRC_DIR = "vhost-device-gpio"

inherit cargo pkgconfig systemd

SYSTEMD_SERVICE:${PN}:gen5 = "vhost-device-gpio.service"

do_install:append:gen5() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${S}/vhost-device-gpio/vhost-device-gpio_sa8797.service ${D}${systemd_system_unitdir}/vhost-device-gpio.service
}

include vhost-device-crates.inc

CARGO_BUILD_FLAGS:remove = "--frozen"
