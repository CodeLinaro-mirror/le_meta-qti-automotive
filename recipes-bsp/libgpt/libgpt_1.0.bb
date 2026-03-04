SUMMARY = "libgpt"
DESCRIPTION = "libgpt library"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=b796c0007db682166a1721da80267bb2"

PR = "r1"

SRC_URI = "${PATH_TO_REPO}/abctl/.git;protocol=${PROTO};destsuffix=abctl;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/abctl/libgpt"

inherit autotools
