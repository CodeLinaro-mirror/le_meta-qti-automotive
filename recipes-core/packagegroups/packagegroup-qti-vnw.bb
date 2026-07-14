SUMMARY = "QTI package group for vichle network"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-vnw \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    ${@bb.utils.contains('PREFERRED_PROVIDER_virtual/kernel', 'linux-ark', 'hsi2s-qmi-test', '', d)} \
    gptp \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'ptp-virtual', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'avb-utils', '', d)} \
    ${@bb.utils.contains('PREFERRED_PROVIDER_virtual/kernel', 'linux-ark', 'aurix-can iproute2', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'hsi2s-be', '', d)} \
    "

# avb-utils is masked out for qclinux-gvm-gen5/gvm-gen5 (see GVM_MISSING_RECIPE_SRCLIST in
# qclinux-bbmask.inc), so drop it from the qti-hypervisor RDEPENDS on those machines
RDEPENDS:${PN}:remove:qclinux-gvm-gen5 = "avb-utils"
RDEPENDS:${PN}:remove:qclinux-gvm-gen5 = "ptp-virtual"

