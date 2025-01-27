SUMMARY = "scmi-test"
DESCRIPTION = "Applications to test scmi framework and gearvm"
HOMEPAGE = "https://git.codelinaro.org/"

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"
DEPENDS += "safelinux-cfg-modules virtual/kernel-headers glib-2.0"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/safelinux-services/scmi-test/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/safelinux-services/scmi-test;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/safelinux-services/scmi-test"

inherit pkgconfig cmake
