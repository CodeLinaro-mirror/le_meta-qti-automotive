SUMMARY = "check for bashisms in /bin/sh scripts "
HOMEPAGE = "https://salsa.debian.org/debian/devscripts"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=faa39cbd7a7cded9a1436248295de3c2"

DEPENDS += "perl"

SRC_URI = "git://github.com/Debian/devscripts.git;protocol=https;branch=master"

SRCREV = "d6f7f6927cc1035ca3fb7ab6ab86b3c7430e6d00"

S = "${WORKDIR}/git"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${bindir}
    install -d ${D}${datadir}

    install -m 0755 ${S}/scripts/checkbashisms.bash_completion ${D}${bindir}
    install -m 0755 ${S}/scripts/checkbashisms.pl ${D}${bindir}
    # enforce usage of sysroot perl instead of host sided
    sed -i "s|/usr/bin/perl|/usr/bin/env perl|g" ${D}${bindir}/checkbashisms.pl
}

FILES:${PN} += "${bindir} ${datadir}"

BBCLASSEXTEND = "native nativesdk"
