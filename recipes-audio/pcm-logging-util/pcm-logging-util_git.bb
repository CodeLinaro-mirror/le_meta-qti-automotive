SUMMARY = "PCM Logging Utility"
DESCRIPTION = "This is a test application to exercise SPF PCM logging settings"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=b796c0007db682166a1721da80267bb2"

DEPENDS += "agm"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/audio-ar-service/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/audio-ar-service/audio_driver/pcm_logging_util;subpath=audio_driver/pcm_logging_util;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/audio-ar-service/audio_driver/pcm_logging_util"

inherit cmake pkgconfig

CFLAGS += "-I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
