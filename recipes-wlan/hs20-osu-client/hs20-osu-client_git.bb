SUMMARY = "Sign up tool for WFA pre-cert test"
DESCRIPTION = "Hs20-osu-client is a sign up tool to obtain the certificate \
               for passpoint， especially for WFA pre-cert test. \
              "
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/external/wpa_supplicant_8/COPYING;md5=5ebcb90236d1ad640558c3d3cd3035df"

DEPENDS += "curl libnl libxml2 pkgconfig"
SRC_URI = "${PATH_TO_REPO}/external/wpa_supplicant_8/.git;protocol=${PROTO};destsuffix=external/wpa_supplicant_8;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/external/wpa_supplicant_8/hs20/client"

inherit autotools-brokensep pkgconfig

CFLAGS += "-I${STAGING_INCDIR}/libxml2/"

EXTRA_OEMAKE:append = " BINDIR=${sbindir}"
