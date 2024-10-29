SUMMARY = "QTI package group for vichle network"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-vnw \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    hsi2s \
    hsi2s-test \
    ${@bb.utils.contains('PREFERRED_PROVIDER_virtual/kernel', 'linux-ark', 'hsi2s-qmi-test', '', d)} \
    gptp \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'ptp-virtual', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'avb-utils', '', d)} \
    ${@bb.utils.contains('PREFERRED_PROVIDER_virtual/kernel', 'linux-ark', 'aurix-can iproute2', '', d)} \
    "

RDEPENDS:${PN}:append:sa81x5 = " gptp open-avb"

RDEPENDS:${PN}:remove:sa8775-flex = "hsi2s \
    hsi2s-test"
RDEPENDS:${PN}:remove:sa8255-ivi = "hsi2s \
    hsi2s-test"
RDEPENDS:${PN}:remove:sa8650-adas = "hsi2s \
    hsi2s-test"
