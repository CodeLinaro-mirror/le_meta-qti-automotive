SUMMARY = "hsi2s-qmi-test"
DESCRIPTION = "Application to enable/disable hsi2s ADSP clock via qmi framework."
HOMEPAGE = "https://git.codelinaro.org/"

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
                    file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "hsi2s-qmi"

PROVIDES = "hsi2s-qmi-test"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/hsi2s/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/hsi2s;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/hsi2s/apps"

inherit autotools-brokensep

TARGET_CC_ARCH += "${LDFLAGS}"
