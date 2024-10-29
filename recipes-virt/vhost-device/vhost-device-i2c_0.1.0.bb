SUMMARY = "vhost i2c backend device"
DESCRIPTION = "A vhost-user backend that emulates a VirtIO I2C device. The daemon provided by rust-vmm/vhost-device."
HOMEPAGE = "https://github.com/rust-vmm/vhost-device"
LICENSE = "Apache-2.0 | BSD-3-Clause"
LIC_FILES_CHKSUM = "\
    file://LICENSE-APACHE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
    file://LICENSE-BSD-3-Clause;md5=2489db1359f496fff34bd393df63947e \
"

SRC_URI = "${PATH_TO_REPO}/external/vhost-device/.git;protocol=${PROTO};destsuffix=external/vhost-device;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/external/vhost-device"
CARGO_SRC_DIR = "vhost-device-i2c"

inherit cargo
include vhost-device-i2c-crates.inc
