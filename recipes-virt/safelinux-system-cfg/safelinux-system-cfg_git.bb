SUMMARY = "safelinux system configuration"
DESCRIPTION = "Add default system configuration"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

SYSTEMD_SERVICE:${PN} = "\
    vfio-device-probe.service \
"

SRC_URI = "\
    ${PATH_TO_REPO}/vendor/qcom/opensource/safelinux-system-cfg/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/safelinux-system-cfg;usehead=1 \
    file://vm_net.conf \
    file://vfio.conf \
    file://vfio_param.conf \
    file://vmm_pwr_key.conf \
    file://0001-vfio-device-probe-service-remove-delay-after-start-o.patch \
"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/safelinux-system-cfg"

inherit systemd

do_compile[noexec] = "1"

do_install:append() {
    install -m 0755 ${S}/modules-autoload-config/i2cdev.conf -D ${D}${libdir}/modules-load.d/i2cdev.conf
    install -m 0755 ${S}/modules-autoload-config/spidev.conf -D ${D}${libdir}/modules-load.d/spidev.conf
    install -m 0755 ${WORKDIR}/vfio.conf -D ${D}${libdir}/modules-load.d/vfio.conf
    install -m 0755 ${WORKDIR}/vfio_param.conf -D ${D}${sysconfdir}/modprobe.d/vfio.conf

    install -d ${D}${bindir}
    install -m 0644 ${S}/vfio-device-probe/vfio-device-probe.service -D ${D}${systemd_unitdir}/system/vfio-device-probe.service
    install -m 0755 ${S}/vfio-device-probe/vfio-device-bind.sh -D ${D}${bindir}/vfio-device-bind.sh

    install -m 0755 ${WORKDIR}/vm_net.conf -D ${D}${libdir}/modules-load.d/vm_net.conf
    install -m 0755 ${WORKDIR}/vmm_pwr_key.conf -D ${D}${libdir}/modules-load.d/vmm_pwr_key.conf
}

do_install:append:sa7255() {
    install -m 0755 ${S}/vfio-device-probe/sa7255_dev.conf -D ${D}${libdir}/vfio-bind.d/sa7255_dev.conf
}

do_install:append:sa8775() {
    install -m 0755 ${S}/vfio-device-probe/sa8775_dev.conf -D ${D}${libdir}/vfio-bind.d/sa8775_dev.conf
}

do_install:append:sa8797() {
    install -m 0755 ${S}/vfio-device-probe/sa8797_dev.conf -D ${D}${libdir}/vfio-bind.d/sa8797_dev.conf
}

FILES:${PN} += "${libdir}/modules-load.d/*"
FILES:${PN} += "${libdir}/vfio-bind.d/*"
FILES:${PN} += "${sysconfdir}/*"
