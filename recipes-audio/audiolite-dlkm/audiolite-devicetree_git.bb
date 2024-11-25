SUMMARY = "Audiolite device tree overlay"
DESCRIPTION = "Audiolite device tree overlay"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "bison-native oot-dtbo virtual/kernel-headers"

SRC_URI = "\
    ${PATH_TO_REPO}/vendor/qcom/opensource/audiolite/devicetree/.git;protocol=${PROTO};usehead=1 \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/audiolite/devicetree"

inherit qti-techpack

EXTRA_OEMAKE += "\
    AUDIOLITE_DTC_INCLUDE=${STAGING_INCDIR}\ ${STAGING_KERNEL_DIR}/include\ ${S} \
    ${@bb.utils.contains('TARGET_USES_AUDIO_FRAMEWORK', 'audiolite', 'ENABLE_AUDIOLITE_OVERLAY=yes', '', d)} \
"

TECHPACK_DTBS = "\
    sa8775p_audiolite_common.dtbo \
    ${@bb.utils.contains('TARGET_USES_AUDIO_FRAMEWORK', 'audiolite', 'sa8775p_audiolite_overlay.dtbo', '', d)} \
    sa8255p_audiolite_common.dtbo \
    ${@bb.utils.contains('TARGET_USES_AUDIO_FRAMEWORK', 'audiolite', 'sa8255p_audiolite_overlay.dtbo', '', d)} \
    sa8797p_audiolite_common.dtbo \
    ${@bb.utils.contains('TARGET_USES_AUDIO_FRAMEWORK', 'audiolite', 'sa8797p_audiolite_overlay.dtbo', '', d)} \
"
