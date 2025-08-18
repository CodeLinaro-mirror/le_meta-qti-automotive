SUMMARY = "QCrosVM Support"
DESCRIPTION = "It is based on google CrosVM to launch Linux based GVM on QTI platforms."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear & BSD-3-Clause & (Apache-2.0 | MIT) & Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a \
                    file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
                    file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
                    file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS += "cargo-native libcap rust-native rust-llvm-native pkgconfig-native"

SRC_URI = "\
    ${PATH_TO_REPO}/vendor/qcom/opensource/crosvm-gunyah/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/crosvm-gunyah;usehead=1 \
    ${PATH_TO_REPO}/external/crosvm/.git;protocol=${PROTO};destsuffix=external/crosvm;usehead=1 \
    ${PATH_TO_REPO}/external/minijail/.git;protocol=${PROTO};destsuffix=external/minijail;usehead=1 \
    ${PATH_TO_REPO}/external/rust/crates/android_logger/.git;protocol=${PROTO};destsuffix=external/rust/crates/android_logger;usehead=1 \
    ${PATH_TO_REPO}/external/rust/crates/simplelog/.git;protocol=${PROTO};destsuffix=external/rust/crates/simplelog;usehead=1 \
    ${PATH_TO_REPO}/external/rust/crates/vmm_vhost/.git;protocol=${PROTO};destsuffix=external/rust/crates/vmm_vhost;usehead=1 \
"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/crosvm-gunyah"

inherit cargo systemd cargo-update-recipe-crates

require ${BPN}-crates.inc

CFLAGS:append = " -Wno-error=stringop-overflow="

SYSTEMD_SERVICE:${PN} = "qcrosvm.service"
SYSTEMD_SERVICE:${PN}:append:sa8255-ivi = " qcrosvm_lv.service"
SYSTEMD_SERVICE:${PN}:append:sa8775-flex = " qcrosvm_lv.service"

EXTRA_OECMAKE += "\
    -DENABLE_TARGET=${BASEMACHINE} \
"

VM_CONFIG_XML ?= "vm_config_la.xml"

do_install:append() {
    install -d ${D}${sysconfdir}
    install -m 0644 ${S}/vm_config_xml/${VM_CONFIG_XML} ${D}${sysconfdir}/vm_config.xml
}

do_install:append:sa8797() {
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/qcrosvm_sa8797.service ${D}/${systemd_unitdir}/system/qcrosvm.service
}

do_install:append:sa8775() {
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/qcrosvm.service ${D}/${systemd_unitdir}/system/qcrosvm.service
}

do_install:append:sa7255() {
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/qcrosvm_sa7255.service ${D}/${systemd_unitdir}/system/qcrosvm.service
}

do_install:append:sa8255-ivi() {
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/qcrosvm_lv.service ${D}/${systemd_unitdir}/system/qcrosvm_lv.service
}

do_install:append:sa8775-flex() {
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/qcrosvm_lv.service ${D}/${systemd_unitdir}/system/qcrosvm_lv.service
}

