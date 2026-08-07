SUMMARY = "sound card info Headers Export"
DESCRIPTION = "This is sound card info header files recipe"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/audio-virtio-be/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/audio-virtio-be/sound_card_info_util_header;subpath=sound_card_info_util_header;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/audio-virtio-be/sound_card_info_util_header"

inherit cmake pkgconfig

SOLIBS = ".so"
FILES_SOLIBSDEV = ""