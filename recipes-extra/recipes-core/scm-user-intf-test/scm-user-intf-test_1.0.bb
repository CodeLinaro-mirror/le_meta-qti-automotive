SUMMARY = "scm-user-intf-test"
DESCRIPTION = "Unit tests for the scm_user_intf driver (/dev/scmnode)"
HOMEPAGE = "https://git.codelinaro.org/"

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"
DEPENDS += "safelinux-cfg-modules virtual/kernel-headers"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/safelinux-services/scm-user-intf-test/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/safelinux-services/scm-user-intf-test;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/safelinux-services/scm-user-intf-test"

inherit pkgconfig cmake
