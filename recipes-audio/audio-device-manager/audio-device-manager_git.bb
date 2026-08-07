SUMMARY = "Audio Device Manager"
DESCRIPTION = "This library manages the interactions of registering the sound card, and receiving and sending device controls from/to the Virtio audio driver"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "libuhab audio-log-util sound-card-info-common-header compute-resmgr"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/audio-virtio-be/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/audio-virtio-be/audio_device_manager;subpath=virtio_audio_device/audio_device_manager;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/audio-virtio-be/audio_device_manager"

CXXFLAGS += "-I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}"

inherit cmake pkgconfig

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
RDEPENDS:${PN} += "audio-plugin-intf sound-card-info-util"