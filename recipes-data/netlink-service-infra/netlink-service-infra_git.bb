SUMMARY = "Implementing the netlink service"
DESCRIPTION = "Implement the netlink service to get the ethernet interface LINK status and pass it to GVM over socket."
HOMEPAGE = "https://git.codelinaro.org"
SECTION = "network"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

SRC_URI:append = " \
    file://0001-PATCH-meta-qti-bsp-Add-the-netlink-service.patch \
    file://netlink-service.service \
"
S = "${WORKDIR}"

inherit cmake systemd

do_configure:prepend() {
    cd ${S}
    rm -f ${S}/meta-qti-base/recipes-data/netlink-service-infra/files/CMakeLists.txt
    rm -f ${S}/meta-qti-base/recipes-data/netlink-service-infra/files/netlink-service.c
    patch -p1 < ${WORKDIR}/0001-PATCH-meta-qti-bsp-Add-the-netlink-service.patch || exit 1
    mv ${S}/meta-qti-base/recipes-data/netlink-service-infra/files/CMakeLists.txt ${S}/CMakeLists.txt
    mv ${S}/meta-qti-base/recipes-data/netlink-service-infra/files/netlink-service.c ${S}/netlink-service.c
}

do_configure() {
    cmake ${S} -B${B} -DCMAKE_INSTALL_PREFIX=${D}
}

do_compile() {
    cmake --build ${B} --target all -- VERBOSE=1
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/netlink-service ${D}${bindir}/netlink-service
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/netlink-service.service ${D}${systemd_system_unitdir}/netlink-service.service
}

SYSTEMD_SERVICE:${PN} = "netlink-service.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"
