FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:class-native = " file://x86-work-around-old-GCC-versions-pre-9.0-having-broken-xgetbv-implementations.patch "
