FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
    file://0001-Aarch64-Add-memcpy-for-qualcomm-s-oryon-1-core.patch \
    ${@bb.utils.contains('SRCREV_glibc', '662516aca8b6bf6aa6555f471055d5eb512b1ddc', 'file://0001-Aarch64-Add-new-memset-for-Qualcomm-s-oryon-1-core.patch', 'file://0002-Aarch64-Add-new-memset-for-Qualcomm-s-oryon-1-core.patch', d)} \
"
