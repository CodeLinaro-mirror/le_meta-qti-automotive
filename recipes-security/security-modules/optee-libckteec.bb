SUMMARY = "PKCS11 OP-TEE Client library"
DESCRIPTION = "OP-TEE PKCS#11 (Public-Key Cryptography Standard #11) Client-side library."
HOMEPAGE = "https://github.com/OP-TEE/optee_client"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;beginline=7;endline=27;md5=f6c51da2169a596879f7e3714c4e47b3"

DEPENDS = "securemsm-noship"

SRC_URI = "git://github.com/OP-TEE/optee_client.git;protocol=https;branch=master"
SRC_URI:append = " file://0001-libckteec-Replace-libteec-library-with-libgptee.patch"

SRCREV = "3eac340a781c00ccd61b151b0e9c22a8c6e9f9f0"

S = "${WORKDIR}/git"

inherit pkgconfig

do_compile() {
    oe_runmake -C ${S}/libckteec
}

do_install:append() {
    install -d ${D}${libdir}
    install -m 755 ${S}/out/libckteec/libckteec.* ${D}${libdir}/
    install -d ${D}/${includedir}
    install -m 0755 ${S}/libckteec/include/* ${D}${includedir}/
}

FILES:${PN} += "${libdir}/libckteec.so*"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""
