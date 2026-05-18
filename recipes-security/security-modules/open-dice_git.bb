SUMMARY = "Open-Dice library"
DESCRIPTION = "Open-Dice library"
HOMEPAGE = "https://github.com/google/open-dice"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "openssl"

SRC_URI = "git://github.com/google/open-dice.git;protocol=https;branch=main"
SRC_URI:append = " file://0001-Port-open-dice-project-to-Linux-environment.patch"

SRCREV = "49a4204a6596363b9c35bde51c2ba048252b4772"

S = "${WORKDIR}/git"

inherit pkgconfig

do_compile() {
    oe_runmake -C ${S}
}

do_install:append() {
    install -d ${D}/${libdir}

    # Install the real library
    install -m 0644 ${S}/libopendice.so.1.0.0 ${D}${libdir}/

    # SONAME symlink
    ln -sf libopendice.so.1.0.0 ${D}${libdir}/libopendice.so.1

    # Linker symlink
    ln -sf libopendice.so.1 ${D}${libdir}/libopendice.so

    install -d ${D}/${includedir}
    install -d ${D}/${includedir}/dice
    install -d ${D}/${includedir}/dice/ops
    install -m 0644 ${S}/include/dice/dice.h ${D}${includedir}/dice/
    install -m 0644 ${S}/include/dice/cbor_writer.h ${D}${includedir}/dice/
    install -m 0644 ${S}/include/dice/ops.h ${D}${includedir}/dice/
    install -m 0644 ${S}/include/dice/ops/clear_memory.h ${D}${includedir}/dice/ops
    install -m 0644 ${S}/include/dice/android.h ${D}${includedir}/dice/
    install -m 0644 ${S}/include/dice/types.h ${D}${includedir}/dice/
    install -m 0644 ${S}/include/dice/config/boringssl_ed25519/dice/config.h ${D}${includedir}/dice/
}

