SUMMARY = "Hyp udmabuf test"
DESCRIPTION = "This is the hyp udmabuf test used to test hyp dmabuf"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "libkiumd"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/hyp-udmabuf/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/hyp-udmabuf;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/hyp-udmabuf/test"

inherit cmake

