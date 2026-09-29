SUMMARY = "QTI package group for security"
DESCRIPTION = "This is the minimal set of packages required for linux userspace security utilities."

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-security \
"

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    libcap \
    libcap-bin \
    attr \
    ${@bb.utils.contains('DISTRO_FEATURES', 'qti-fde', 'enable-fde', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'securemsmdlkm', '', d)}  \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'securemsm-devicetree', '', d)}  \
    optee-libckteec  \
"

RDEPENDS:${PN}:remove:sa8650-adas = "optee-libckteec"
