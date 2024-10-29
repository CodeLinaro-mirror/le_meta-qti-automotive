SUMMARY = "systemd target for early services"
DESCRIPTION = "\
This systemd target is a synchronization point for all early services. \
Early services shall be configured to start Before=early-services.target"

HOMEPAGE = "https://git.codelinaro.org"

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "systemd bootkpi-logging"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/safelinux-services/early-service-infra/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/safelinux-services/early-service-infra;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/safelinux-services/early-service-infra"

inherit pkgconfig cmake systemd

SYSTEMD_SERVICE:${PN} = "early-services.target"

EXTRA_OECMAKE += "-DEXAMPLE_SERVICE:BOOL=OFF"
