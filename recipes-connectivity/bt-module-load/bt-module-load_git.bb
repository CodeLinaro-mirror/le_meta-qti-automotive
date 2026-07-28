SUMMARY = "Bluetooth Kernel Module Load Script"
DESCRIPTION = "For HGY LV PVM, Bluetooth power is controlled by GearVM.\
The Bluetooth kernel stack depends on btpower module to power on the chipset.\
To avoid early initialization before BT is powered on, hci_uart is disabled\
from automatic loading. Instead it will be loaded by this systemd serivce."
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

SRC_URI = "file://load_bt_modules.sh \
           file://bt_module_load.service \
           file://bt_module_load_legacy.service"

inherit systemd

SYSTEMD_SERVICE:${PN} = "bt_module_load.service"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${nonarch_base_libdir}/firmware/
    ln -sf /bluetooth/image ${D}${nonarch_base_libdir}/firmware/qca
    install -d ${D}${bindir}
    install -D -m 0755 ${WORKDIR}/load_bt_modules.sh ${D}${bindir}/
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${WORKDIR}/bt_module_load.service -D ${D}${systemd_unitdir}/system/
}

do_install:append:sa8255-ivi() {
    install -m 0644 ${WORKDIR}/bt_module_load_legacy.service -D ${D}${systemd_unitdir}/system/bt_module_load.service
}

do_install:append:sa7255-ivi() {
    install -m 0644 ${WORKDIR}/bt_module_load_legacy.service -D ${D}${systemd_unitdir}/system/bt_module_load.service
}

FILES:${PN} += "${bindir}/load_bt_modules.sh \
                ${nonarch_base_libdir}/firmware/qca \
                ${systemd_unitdir}/system/* \
"

RDEPENDS:${PN} += "rfkill"

