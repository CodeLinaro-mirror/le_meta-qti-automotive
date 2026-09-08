SUMMARY = "Library for BPF handling"
HOMEPAGE = "https://github.com/libbpf/libbpf"
SECTION = "libs"
LICENSE = "LGPL-2.1-only | BSD-2-Clause"

LIC_FILES_CHKSUM = "file://../LICENSE.LGPL-2.1;md5=b370887980db5dd40659b50909238dbd \
                    file://../LICENSE.BSD-2-Clause;md5=67cb4c623fc9a9acea2f039dad14f71a"

DEPENDS = "zlib elfutils"

SRC_URI = "git://github.com/libbpf/libbpf.git;protocol=https;branch=master \
           file://0001-libbpf-Fix-tracepoint-perf_event_open-failing-with-ENODEV-on-offline-CPUs.patch;striplevel=2 \
           "
SRCREV = "f5dcbae736e5d7f83a35718e01be1a8e3010fa39"

S = "${WORKDIR}/git/src"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_HOST = "(x86_64|i.86|arm|aarch64|riscv64|powerpc|powerpc64|mips64).*-linux"

EXTRA_OEMAKE += "DESTDIR=${D} LIBDIR=${libdir} INCLUDEDIR=${includedir}"
EXTRA_OEMAKE:append:class-native = " UAPIDIR=${includedir}"

inherit pkgconfig

do_compile() {
    oe_runmake
}

do_install() {
    oe_runmake install
}

do_install:append:class-native() {
    oe_runmake install_uapi_headers
}

BBCLASSEXTEND = "native nativesdk"
