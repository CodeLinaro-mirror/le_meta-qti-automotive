SUMMARY = "AudioReach configurations"
DESCRIPTION = "This project provides business entity level, business application level, and platform level kvh2xml and/or card-defs xml"
HOMEPAGE = "https://github.com/Audioreach/audioreach-conf"

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/audioreach-conf/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/audioreach-conf;subpath=audioreach-conf;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/vendor/qcom/opensource/audioreach-conf"

EXTRA_OECONF += "--with-qcom"

EXTRA_OECONF:append:gen5 = " --with-automotive --with-sa8797"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit autotools pkgconfig

do_compile[noexec] = "1"

FILES_${PN} += "${sysconfdir}/card-defs.xml"
