SUMMARY = "Qualcomm Compute Node"
DESCRIPTION = "The QCNode is Qualcomm ADAS hardware abstraction layer that provides user friendly APIs \
         to access the Qualcomm ADAS hardware accelerator. It is ADAS service oriented to provide \
         user friendly component and utils to cover the perception and compute vision related task."
HOMEPAGE = "https://github.com/qualcomm/QcNode"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=7a434440b651f4a472ca93716d01033a"

# Note: if want to build qcnode with QNN SDK, update the QNN_SDK_ROOT to "/path/to/qnn_sdk"
QNN_SDK_ROOT = ""

# QCNODE Build Configurations
QCNODE_ENABLE_C2D = "OFF"
QCNODE_ENABLE_EVA = "ON"
QCNODE_ENABLE_EVA_AUTO = "OFF"
QCNODE_ENABLE_DEMUXER = "OFF"
QCNODE_ENABLE_FADAS = "ON"
QCNODE_ENABLE_RSM_V2 = "OFF"
QCNODE_ENABLE_C2C = "OFF"
QCNODE_ENABLE_TRACE = "OFF"
QCNODE_ENABLE_GENIE = "OFF"
QCNODE_ENABLE_RADAR = "OFF"
QCNODE_ENABLE_RESMON = "OFF"
QCNODE_QC_TARGET_SOC = "8797"

DEPENDS += "aosal apdf fastrpc libkiumd libstd rpcmem camera-qcx fadas adreno virtual/kernel-headers mm-osal video-driver gbm gbm-headers drm gtest libbsd \
            nlohmann-json sv-auto sv-auto-noship safetylibs"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/qcnode/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/qcnode;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/vendor/qcom/opensource/qcnode"

inherit cmake

CFLAGS += "\
    -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel} \
"

CXXFLAGS += "\
    -I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel} \
"

EXTRA_OECMAKE += "-DCMAKE_SYSROOT=${STAGING_DIR_HOST} \
    -DCMAKE_INSTALL_PREFIX=/usr \
"

# Conditionally append only if QNN_SDK_ROOT is not empty
EXTRA_OECMAKE += "${@'-DQNN_SDK_ROOT=${QNN_SDK_ROOT}' if d.getVar('QNN_SDK_ROOT') else ''}"

# List the BitBake variables want to process
QCNODE_CMAKE_VARS = "QCNODE_ENABLE_C2D \
    QCNODE_ENABLE_EVA \
    QCNODE_ENABLE_EVA_AUTO \
    QCNODE_ENABLE_DEMUXER \
    QCNODE_ENABLE_FADAS \
    QCNODE_ENABLE_RSM_V2 \
    QCNODE_ENABLE_C2C \
    QCNODE_ENABLE_TRACE \
    QCNODE_ENABLE_GENIE \
    QCNODE_ENABLE_RADAR \
    QCNODE_ENABLE_RESMON \
    QCNODE_QC_TARGET_SOC \
"

# Append to EXTRA_OECMAKE
# Logic:
#   v = "QCNODE_ENABLE_EVA"
#   v.replace("QCNODE_", "") -> "ENABLE_EVA"
#   d.getVar(v) -> "OFF" (or "ON" if set)
# Result: -DENABLE_EVA=ON
EXTRA_OECMAKE += "${@' '.join(['-D%s=%s' % (v.replace('QCNODE_', ''), d.getVar(v)) for v in d.getVar('QCNODE_CMAKE_VARS').split()])}"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

INSANE_SKIP:${PN} += "\
    arch \
    already-stripped \
    file-rdeps \
    ldflags \
    libdir \
"

INSANE_SKIP:${PN}-dbg += "\
    libdir \
"

FILES:${PN} += "\
    /usr/lib/dsp \
    /usr/lib/dsp/*.so \
    /usr/lib/dsp/v68 \
    /usr/lib/dsp/v73 \
    /usr/lib/dsp/v75 \
"

FILES:${PN}-dev += "\
    ${bindir}/*gtest* \
    ${includedir}/* \
    ${libdir}/cmake \
"

