SUMMARY = "AOSP Build Tools"
DESCRIPTION = "Kernel AOSP build Tools for create boot image"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

BASE_GIT_PATH = "${PATH_TO_REPO}/kernel/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform"
BASE_PATH = "kernel/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform"

SRC_URI = "${BASE_GIT_PATH}/build/kernel/.git;protocol=${PROTO};destsuffix=${BASE_PATH}/build/kernel \
           ${BASE_GIT_PATH}/prebuilts/kernel-build-tools/.git;protocol=${PROTO};destsuffix=${BASE_PATH}/prebuilts/kernel-build-tools \
           "

SRCREV = "${AUTOREV}"

S = "${WORKDIR}"

inherit native

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
    cd ${S}/${BASE_PATH}/build/kernel/android
    install -d ${D}/${bindir}/build/android/

    for SC_FILE in `ls *.py *.sh`; do
        install -D ${S}/${BASE_PATH}/build/kernel/android/${SC_FILE} ${D}/${bindir}/build/android/
    done

    install -d ${D}/${bindir}/build/prebuilts
    cp -rf ${S}/${BASE_PATH}/prebuilts/kernel-build-tools ${D}/${bindir}/build/prebuilts
}
