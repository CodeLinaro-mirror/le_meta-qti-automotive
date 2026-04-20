SUMMARY = "Library for memory allocator functions for dmabufheap"
DESCRIPTION = "Build Android libdmabufheap library for LV. \
DMA-BUF heaps is an alternative for ION that has made it into the upstream kernel. \
which offers greater control over who in user space \
can perform allocations by using SELinux policies "
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS += "libbase libion virtual/kernel-headers"

SRC_URI = "${PATH_TO_REPO}/src/system/memory/libdmabufheap/.git;protocol=${PROTO};destsuffix=src/system/memory/libdmabufheap;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/src/system/memory/libdmabufheap"

inherit autotools-brokensep pkgconfig

CPPFLAGS += "-I${STAGING_INCDIR}/ion_headers"

EXTRA_OECONF:append = " --with-sanitized-headers=${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}/"

PACKAGE_ARCH = "${MACHINE_ARCH}"

