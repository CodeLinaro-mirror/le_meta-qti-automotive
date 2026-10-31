SUMMARY = "AR Audio Log Utility"
DESCRIPTION = "Shared AR audio logging library with runtime log-level configuration."
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "glib-2.0"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/audio-ar-service/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/audio-ar-service;usehead=1"
SRCREV = "${AUTOREV}"

RDEPENDS:${PN} += "glib-2.0"

S = "${WORKDIR}/vendor/qcom/opensource/audio-ar-service/audio_driver/audio_log_util"

inherit cmake pkgconfig

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
