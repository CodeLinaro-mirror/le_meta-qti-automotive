SUMMARY = "Simulator that generates CAN messages of a driving car"
HOMEPAGE = "https://gerrit.automotivelinux.org/gerrit/gitweb?p=AGL/meta-agl-demo.git;a=tree;f=recipes-demo/simple-can-simulator;h=6cd9ded2125eb05eb10d37abec2fbb4b4658f18d;hb=refs/heads/salmon"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/simple_can_simulator.py;beginline=2;endline=22;md5=9bb9c582301261d21460d2bc5bb8c225"

SRC_URI = "file://simple_can_simulator.py"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${WORKDIR}/simple_can_simulator.py ${D}${sbindir}
}

RDEPENDS:${PN} = "python3"
