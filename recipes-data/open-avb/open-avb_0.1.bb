SUMMARY = "Open AVB"
DESCRIPTION = "Open Source Project for Audio Video Bridging/Time Sensitive Networking stack"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "\
    alsa-lib cmake-native glib-2.0 libpcap \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'libuhab', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'qti-gstqeavb', 'gstreamer1.0 gstreamer1.0-plugins-base', '', d)} \
    pciutils \
"

SRC_URI = "\
    ${PATH_TO_REPO}/external/open-avb/.git;protocol=${PROTO};destsuffix=external/open-avb;usehead=1 \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/external/open-avb"

inherit systemd pkgconfig

EXTRA_OEMAKE += "${@bb.utils.contains("DISTRO_FEATURES", "systemd", "SYSTEMD_SUPPORT_INCLUDED=1", "SYSTEMD_SUPPORT_INCLUDED=0", d)}"
SYSTEMD_AUTO_ENABLE:${PN} = "disable"
PACKAGE_ARCH = "${MACHINE_ARCH}"

TARGET_CC_ARCH += "${LDFLAGS}"

do_compile() {
    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'true', 'false', d)}; then
        export AVB_FEATURE_GVM_MODE=1
    fi

    export AVB_FEATURE_NEUTRINO=1
    export AVB_FEATURE_INTF_ALSA2=0

    if ${@bb.utils.contains('DISTRO_FEATURES', 'qti-gstqeavb', 'true', 'false', d)}; then
        export AVB_FEATURE_GSTREAMER=1
        export GSTREAMER_1_0=1
    fi

    echo ${FILESEXTRAPATHS}
    echo ${subdir}

    mkdir -p ${S}/daemons/maap/build
    oe_runmake mrpd
    oe_runmake maap
    make avtp_pipeline
}

do_install() {
    install -d ${D}/${bindir}/
    install -d ${D}/${bindir}/avb/
    install -d ${D}/${libdir}/
    install -d ${D}/${includedir}/
    install -m 0755 ${S}/daemons/maap/linux/maap_daemon ${D}/${bindir}/avb
    install -m 0755 ${S}/daemons/mrpd/mrpd ${D}/${bindir}/avb
    install -m 0755 ${S}/daemons/mrpd/mrpctl ${D}/${bindir}/avb
    install -m 0755 ${S}/lib/avtp_pipeline/build/bin/* ${D}/${bindir}/avb
    install -m 0755 ${S}/lib/avtp_pipeline/build/lib/*.so ${D}/${libdir}

}

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
