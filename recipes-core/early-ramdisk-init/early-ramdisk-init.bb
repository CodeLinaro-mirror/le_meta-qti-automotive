SUMMARY = "early-ramdisk-init for load kernel modules and start rootfs init"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"
DEPENDS = "kmod util-linux"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/early-ramdisk-init/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/early-ramdisk-init;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/early-ramdisk-init"

inherit autotools

EXTRA_OECONF += "--bindir=${base_sbindir} --sbindir=${base_sbindir}"

CFLAGS += '-DLOG_DIR=\\"/boot/early-ramdisk\\"'
CFLAGS += "${@bb.utils.contains('DISTRO_FEATURES', 'early_init', '-DEARLY_INIT', '', d)}"

TARGET_PATH_NAME ?= "${MACHINE}"
TARGET_PATH_NAME:sa8775 = "sa8775"
TARGET_PATH_NAME:sa7255 = "sa7255"

do_install:append() {
    install -d ${D}/dev
    install -d ${D}/sys
    install -d ${D}/etc
    install -d ${D}/proc
    install -d ${D}/boot/early-ramdisk
    install -d ${D}/realroot
    install -d ${D}/etc/modules-load.f
    touch ${D}/init
    install -m 0755 ${S}/conf/${TARGET_PATH_NAME}/*.conf -D ${D}/etc/modules-load.f/
    if ${@bb.utils.contains('DISTRO_FEATURES', 'qti-external-boot', 'true', 'false', d)}; then
        install -m 0755 ${S}/conf/${TARGET_PATH_NAME}/02-external-bootup.conf.in -D ${D}/etc/modules-load.f/02-external-bootup.conf
    fi
}

FILES:${PN} += "\
         init \
         dev/ \
         sys/ \
         etc/ \
         proc/ \
         boot/early-ramdisk \
         realroot/ \
         etc/modules-load.f/* \
"
