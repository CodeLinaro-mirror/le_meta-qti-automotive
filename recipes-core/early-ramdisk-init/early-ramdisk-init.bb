SUMMARY = "early-ramdisk-init for load kernel modules and start rootfs init"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"
DEPENDS = "kmod util-linux"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/early-ramdisk-init/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/early-ramdisk-init;usehead=1"
SRC_URI:append = "${@bb.utils.contains("MACHINE_FEATURES", "qti-umd", " file://vfio_param.conf", "", d)}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/early-ramdisk-init"

inherit autotools

EXTRA_OECONF += "--bindir=${base_sbindir} --sbindir=${base_sbindir}"

CFLAGS += '-DLOG_DIR=\\"/boot/early-ramdisk\\"'
CFLAGS += "${@bb.utils.contains('DISTRO_FEATURES', 'early_init', '-DEARLY_INIT', '', d)}"
CFLAGS:append:sa8775 = " ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', '-DVFIO_BIND_DEVICE -DVENDOR_DSP_MOUNT -DFIRMWARE_MOUNT -DPRELOAD_UNIT', '', d)}"
CFLAGS:append:sa7255 = " ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', '-DVFIO_BIND_DEVICE', '', d)}"
CFLAGS:append:gen5 = " ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', '-DVFIO_BIND_DEVICE -DMM_VFIO_BIND_DEVICE -DVENDOR_DSP_MOUNT -DFIRMWARE_MOUNT -DPRELOAD_UNIT', '', d)}"

TARGET_PATH_NAME ?= "${MACHINE}"
TARGET_PATH_NAME:sa8775 = "sa8775-qclinux"
TARGET_PATH_NAME:sa7255 = "sa7255"
TARGET_PATH_NAME:sa7255-ivi = "sa7255-qclinux"
TARGET_PATH_NAME:gen5 = "sa8797"

do_install:append() {
    install -d ${D}/dev
    install -d ${D}/sys
    install -d ${D}/etc
    install -d ${D}/proc
    install -d ${D}/boot/early-ramdisk
    install -d ${D}/realroot
    install -d ${D}/etc/modules-load.f
    install -d ${D}/etc/modules-load.l
    touch ${D}/init
    install -m 0755 ${S}/conf/${TARGET_PATH_NAME}/*.conf -D ${D}/etc/modules-load.f/
    install -m 0755 ${S}/conf/${TARGET_PATH_NAME}/*.late -D ${D}/etc/modules-load.l/
    if ${@bb.utils.contains('DISTRO_FEATURES', 'qti-external-boot', 'true', 'false', d)}; then
        # External hdd root device node is detected by 00-external-bootup.conf load done.
        install -m 0644 ${S}/conf/${TARGET_PATH_NAME}/02-external-bootup.conf.in -D ${D}/etc/modules-load.f/00-external-bootup.conf
    fi
    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'true', 'false', d)}; then
        install -m 0755 ${WORKDIR}/vfio_param.conf -D ${D}${sysconfdir}/modprobe.d/vfio.conf
    fi
}

do_install:append:sa8775() {
    install -m 0644 ${S}/conf/${TARGET_PATH_NAME}/07-gvm.conf.in -D ${D}/etc/modules-load.f/07-gvm.conf
}

do_install:append:sa7255-ivi() {
    install -m 0644 ${S}/conf/${TARGET_PATH_NAME}/07-gvm.conf.in -D ${D}/etc/modules-load.f/07-gvm.conf
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
