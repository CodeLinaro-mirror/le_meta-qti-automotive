SUMMARY = "Android utils library for C"
DESCRIPTION = "This library provides set of fundamental routines which are \
essential to basically any Unix utility or daemon application written in C."
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=9645f39e9db895a4aa6e02cb57294595"

DEPENDS += "liblog"

PR = "r1"

SRC_URI = "\
    ${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=system/core;usehead=1 \
    file://0001-libcutils-Remove-autotools-support.patch;patchdir=../ \
    https://git.codelinaro.org/clo/la/platform/system/core/-/commit/c2d8aad8d70aeb4d50f077f552044b85ef6c64b9.patch;downloadfilename=0001-libcutils-ashmem-fortify-and-comply-with-Android-cod.patch;patchdir=../;name=patch1 \
    https://git.codelinaro.org/clo/la/platform/system/core/-/commit/1186f3a5ad6581fae6e284fef4bfcefe50462cda.patch;downloadfilename=0002-libcutils-ashmem-check-fd-validity.patch;patchdir=../;name=patch2 \
    https://git.codelinaro.org/clo/la/platform/system/core/-/commit/e37111d7516827489232c6c894e114a58952fe4a.patch;downloadfilename=0003-libcutils-ashmem-print-error-message-for-invalid-fd.patch;patchdir=../;name=patch3 \
    https://git.codelinaro.org/clo/la/platform/system/core/-/commit/53c0ca6520528f53aa9ed3368e5c6fcbd3152851.patch;downloadfilename=0004-libcutils-abort-for-invalid-fd.patch;patchdir=../;name=patch4 \
    https://git.codelinaro.org/clo/la/platform/system/core/-/commit/ee431112ff0d9bab8b4bd4259adc361d46f130cc.patch;downloadfilename=0005-libcutils-Add-ashmem_valid-function.patch;patchdir=../;name=patch5 \
    file://0001-libcutils-Add-autotools-support.patch;patchdir=../ \
"
SRC_URI[patch1.md5sum] = "af8a32f895fc82520a2a52758cc6201e"
SRC_URI[patch1.sha256sum] = "2a4dee643e1e496e91f9a27d794cf42b6c3fc5f6536091bd6ac1b4db9842bdc0"
SRC_URI[patch2.md5sum] = "7183dda3c30b706838a17723e5af3f3a"
SRC_URI[patch2.sha256sum] = "38165f2bc5e8ce2bd3460fb0b06d942c691875ac737146d44413f236853e6bc2"
SRC_URI[patch3.md5sum] = "c72b277717a498040f31b56d18d2d54c"
SRC_URI[patch3.sha256sum] = "e4b6a6dcb16c0c0350fef8957fae5ce992ee27080d27322f6f2a0d898762915e"
SRC_URI[patch4.md5sum] = "47b1e3513f57c20c4d1184f38d4417c3"
SRC_URI[patch4.sha256sum] = "e3bf36a7384889508c7bcd663125353084ba3177043a7ddea717804f6d7dce83"
SRC_URI[patch5.md5sum] = "c96860bcb5aa51249491fc51cf36b805"
SRC_URI[patch5.sha256sum] = "38ae4061a44592cddbcb3c137caaacc59147ee5c2415c87771add55b3893d26e"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/system/core/libcutils"

inherit autotools pkgconfig

EXTRA_OECONF += "\
    --with-core-includes=${WORKDIR}/system/core/include \
    --with-host-os=${HOST_OS} \
    --disable-static \
    LE_PROPERTIES_ENABLED=true \
"

do_install:append() {
    ln -sf ../private/android_filesystem_capability.h ${D}${includedir}/cutils/android_filesystem_capability.h
    ln -sf ../private/android_filesystem_config.h ${D}${includedir}/cutils/android_filesystem_config.h
}

BBCLASSEXTEND = "native"
