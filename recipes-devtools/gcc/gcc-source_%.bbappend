FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " file://0001-AARCH64-Add-Qualcomnm-oryon-1-core.patch \
                   file://0001-Fix-sanitizer-compile-error.patch \
                   file://0001-asan-forbid-allocator64.patch \
"
