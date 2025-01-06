SUMMARY = "Weston SDM Extension Headers"
DESCRIPTION = "Provides QTI specific header files. This package is isolated from weston-sdm-extension \
so that weston could depend on it. See weston-sdm-extension for more information."
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause & MIT & BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
                    file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
                    file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"
CODE_DIR = "${@bb.utils.contains_any('PREFERRED_PROVIDER_virtual/kernel', 'linux-qcom-custom linux-qcom-custom-rt',"vendor/qcom/opensource/display/weston-sdm-extension", "graphics/weston-sdm-extension", d)}"

SRC_URI = "${PATH_TO_REPO}/${CODE_DIR}/.git;protocol=${PROTO};destsuffix=${CODE_DIR};usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/${CODE_DIR}"

PREBUILT = "1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install(){
    install -d ${D}${includedir}
    install -m 0644 ${S}/include/*.h ${D}${includedir}
}

ALLOW_EMPTY:${PN} = "1"
