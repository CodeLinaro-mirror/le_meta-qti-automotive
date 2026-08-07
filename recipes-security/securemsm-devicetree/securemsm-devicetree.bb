SUMMARY = "Security devicetree"

DESCRIPTION = "To provide devicetree attributes for security kernel modules"

HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only | BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6 \
                    file://${COREBASE}/meta/files/common-licenses/BSD-2-Clause;md5=cb641bc04cda31daea161b1bc15da69f"

DEPENDS += "securemsmdlkm"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/securemsm-devicetree/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/securemsm-devicetree;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/securemsm-devicetree"

EXT_MODULE = "vendor/qcom/opensource/securemsm-devicetree"

inherit qti-techpack

do_configure[noexec] = "1"

TECHPACK_MODULE_OUT = "${S}"

TECHPACK_DTBS = "lemans/lemans-quin-vm-securemsm.dtbo monaco/monaco-quin-vm-securemsm.dtbo nord/nord-quin-vm-securemsm.dtbo"
