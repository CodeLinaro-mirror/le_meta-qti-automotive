SUMMARY = "VMM power control utility"
DESCRIPTION = "Send power control commands to a Gunyah VM via the VMM power key service"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "vmm-lib"
SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/crosvm-gunyah/vm-powerctl/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/crosvm-gunyah/vm-powerctl;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/vendor/qcom/opensource/crosvm-gunyah/vm-powerctl"
RDEPENDS:${PN} = "vmm-lib"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit cmake pkgconfig
