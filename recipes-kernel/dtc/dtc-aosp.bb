SUMMARY = "Device Tree Compiler"
DESCRIPTION = "The Device Tree Compiler is a tool used to manipulate the Open-Firmware-like device tree used by PowerPC kernels."
HOMEPAGE = "https://devicetree.org/"
SECTION = "bootloader"
LICENSE = "GPL-2.0-only & (GPL-2.0-only | BSD-2-Clause)"
LIC_FILES_CHKSUM = "file://libfdt/libfdt.h;beginline=4;endline=7;md5=05bb357cfb75cae7d2b01d2ee8d76407"

PROVIDES = "dtc-native"

SRC_URI = "${PATH_TO_REPO}/kernel/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform/external/dtc/.git;protocol=${PROTO};destsuffix=kernel/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform/external/dtc;usehead=1 "

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/kernel/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform/external/dtc"

inherit autotools-brokensep pkgconfig

EXTRA_OEMAKE = "PREFIX=${prefix} DESTDIR=${D} LIBDIR=${libdir} INCLUDEDIR=${includedir}"

PACKAGECONFIG ??= "tools"
PACKAGECONFIG[tools] = "-Dtools=true,-Dtools=false,flex-native bison-native"

PACKAGES =+ "${PN}-misc"
FILES:${PN}-misc = "${bindir}/convert-dtsv0 ${bindir}/ftdump ${bindir}/dtdiff"
RDEPENDS:${PN}-misc += "${@bb.utils.contains('PACKAGECONFIG', 'tools', 'bash diffutils', '', d)}"

BBCLASSEXTEND = "native nativesdk"
