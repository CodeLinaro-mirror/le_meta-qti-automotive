SUMMARY = "A small and portable INI file library with read/write support"
DESCRIPTION = "minIni is a small, portable, single‑file C library for reading \
               and writing INI configuration files. It has no external \
               dependencies and is released under the Apache‑2.0 license."
HOMEPAGE = "https://github.com/compuphase/minIni"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb21481ad45c5578ae8c8d37b8c8d76d"

PV = "1.5+git${SRCPV}"
SRC_URI = "git://github.com/compuphase/minIni.git;branch=master;protocol=https"
SRCREV = "1bb6557030964c921da374e6541e6acb965588e2"
S = "${WORKDIR}/git"

inherit pkgconfig
do_compile() {
    ${CC} ${CFLAGS} ${LDFLAGS} -c ${S}/dev/minIni.c -o minIni.o
    ${CC} ${LDFLAGS} -shared -o libminIni.so minIni.o
}

do_install() {
    # Destination directories
    install -d ${D}${libdir}
    install -d ${D}${includedir}/minini

    install -m 0755 libminIni.so ${D}${libdir}/
    install -m 0644 ${S}/dev/minIni.h ${D}${includedir}/minini/
}

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
