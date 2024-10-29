SUMMARY = "QTI Synergy opensource for AGL Platform"
DESCRIPTION = "Synergy opensource is part of Synergy BT Stack\
which contains BT audio service for HF audio routing."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "cmake-native dbus pulseaudio"

SRC_URI = "${PATH_TO_REPO}/synergy/synergy-opensource/.git;protocol=${PROTO};destsuffix=synergy/synergy-opensource;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/synergy"

do_compile() {
    export CC="${CC}"
    export CROSS_COMPILE=${TARGET_PREFIX}

    # Build synergy-opensource for qca
    if [ -f "${B}/synergy-opensource/platform/msm/makefile" ]; then
        make -C ${B} -f ${B}/synergy-opensource/platform/msm/makefile all IMGRFS=${STAGING_DIR_HOST} CROSS_COMPILE=${TARGET_PREFIX} V=1 CC="${CC}" CHIP_TYPE="QCA"
    fi
}

do_install() {
    install -d ${D}${bindir}

    if [ -f "${B}/synergy-opensource/platform/msm/bt_audio_service/output/bin/bt_audio_service" ]; then
        install -m 0755 ${B}/synergy-opensource/platform/msm/bt_audio_service/output/bin/bt_audio_service ${D}${bindir}
    fi
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
