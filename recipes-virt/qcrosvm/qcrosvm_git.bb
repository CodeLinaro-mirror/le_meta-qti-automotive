SUMMARY = "QCrosVM Support"
DESCRIPTION = "It is based on google CrosVM to launch Linux based GVM on QTI platforms."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear & BSD-3-Clause & (Apache-2.0 | MIT) & Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a \
                    file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
                    file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
                    file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SYSTEMD_SERVICE:${PN} = "qcrosvm.service"

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

inherit ${@bb.utils.contains("BBFILE_COLLECTIONS", "rust-layer", "cargo", "", d)} systemd

CARGO_DISABLE_BITBAKE_VENDORING = "1"

do_install:append:sa8775() {
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/qcrosvm.service ${D}/${systemd_unitdir}/system/qcrosvm.service
}

do_install:append:sa7255() {
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/qcrosvm_sa7255.service ${D}/${systemd_unitdir}/system/qcrosvm.service
}

# Once upgrade Yocto to 4.2 and upgrade Python to 3.11 in the future, we can inherit cargo-update-recipe-crates and delete this line
do_compile[network] = "1"
