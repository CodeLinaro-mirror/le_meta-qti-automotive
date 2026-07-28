SUMMARY = "Bazel build and test tool (Binary Release)"
DESCRIPTION = "Bazel build and test tool - Official Binary"
HOMEPAGE = "https://bazel.build"
LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "8.3.0"

SRC_URI = "https://github.com/bazelbuild/bazel/releases/download/${PV}/bazel-${PV}-linux-x86_64;name=bazel"
SRC_URI[bazel.sha256sum] = "55ba395ed114ee35ee9add63fbc44fa46a6b518b3c2f6d0211a574957cddde2d"

S = "${WORKDIR}"

inherit native

INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"
INSANE_SKIP:${PN} += "already-stripped"

do_compile[noexec] = "1"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 \
        ${WORKDIR}/bazel-${PV}-linux-x86_64 \
        ${D}${bindir}/bazel
}
