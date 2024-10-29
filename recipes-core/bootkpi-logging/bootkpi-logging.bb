SUMMARY = "bootkpi-logging API"
DESCRIPTION = "bootkpi-logging API that abstracts the implementation of bootkpi logging for other userspace apps"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "glibc systemd"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/safelinux-system-cfg/bootkpi-logging/.git;protocol=${PROTO};destsuffix=/vendor/qcom/opensource/safelinux-system-cfg/bootkpi-logging;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/vendor/qcom/opensource/safelinux-system-cfg/bootkpi-logging"

inherit pkgconfig cmake
