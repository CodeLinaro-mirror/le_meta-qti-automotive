FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
    file://0001-Aarch64-Add-memcpy-for-qualcomm-s-oryon-1-core.patch \
    file://0001-Aarch64-Add-new-memset-for-Qualcomm-s-oryon-1-core.patch \
"
