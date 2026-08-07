SUMMARY = "Audio Utils"
DESCRIPTION = "This is a library which provides interface for SMMU helper & workloop functionality"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "audio-log-util audio-headers-export glib-2.0 mm-osal libkiumd virtual/kernel-headers"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/audio-ar-service/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/audio-ar-service/audio_driver/utils;subpath=audio_driver/utils;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/audio-ar-service/audio_driver/utils"

inherit cmake pkgconfig

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
