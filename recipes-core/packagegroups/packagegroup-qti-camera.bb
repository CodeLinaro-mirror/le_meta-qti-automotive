SUMMARY = "QTI package group for camera"
LICENSE = "BSD-3-Clause-Clear"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-camera \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    ${@bb.utils.contains_any("PREFERRED_VERSION_linux-msm", "5.15 6.1", "cameradlkm", "", d)} \
    "

RDEPENDS:${PN}:quin-gvm-lemans = ""
RDEPENDS:${PN}:quin-gvm-monaco = ""
