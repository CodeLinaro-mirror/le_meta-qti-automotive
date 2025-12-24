SUMMARY = "QTI package group for QcNode"
LICENSE = "BSD-3-Clause-Clear"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-qcnode \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "qcnode"
