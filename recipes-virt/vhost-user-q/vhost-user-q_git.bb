SUMMARY = "Vhost user qti binary"
DESCRIPTION = "vhost user qti binary with multimedia services"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

LA_BASIC_SERVICES_LIST = "\
    vhost-user-disp.service \
    vhost-user-gpu.service \
    vhost-user-misc.service \
    vhost-user-aud.service \
    vhost-user-vid.service \
    vhost-user-cam.service \
"

LA_EXTRA_SERVICES_LIST_GEN4_5 = "\
    vhost-user-vnw.service \
    vhost-user-ext.service \
    vhost-user-gpce.service \
"

LA_EXTRA_SERVICES_LIST_GEN5 = "\
    vhost-user-soccp.service \
    vhost-user-dprx.service \
    vhost-user-eva.service \
"

LV_SERVICES_LIST = "\
    vhost-user-disp-vm3.service \
    vhost-user-gpu-vm3.service \
    vhost-user-misc-vm3.service \
    vhost-user-aud-vm3.service \
    vhost-user-vid-vm3.service \
    vhost-user-cam-vm3.service \
    vhost-user-vnw-vm3.service \
    vhost-user-ext-vm3.service \
    vhost-user-gpce-vm3.service \
"

LV_EXTRA_SERVICES_LIST_GEN5 = "\
    vhost-user-soccp-vm3.service \
    vhost-user-dprx-vm3.service \
    vhost-user-eva-vm3.service \
"

SYSTEMD_SERVICE:${PN} = "\
    ${LA_BASIC_SERVICES_LIST} \
"

SYSTEMD_SERVICE:${PN}:append:sa7255-ivi = "\
    ${LV_SERVICES_LIST} \
"

SYSTEMD_SERVICE:${PN}:append:sa8255-ivi = "\
    ${LA_EXTRA_SERVICES_LIST_GEN4_5} \
    ${LV_SERVICES_LIST} \
"

SYSTEMD_SERVICE:${PN}:append:sa8775-flex = "\
    ${LA_EXTRA_SERVICES_LIST_GEN4_5} \
    ${LV_SERVICES_LIST} \
"

SYSTEMD_SERVICE:${PN}:append:gen5 = "\
    ${LA_EXTRA_SERVICES_LIST_GEN4_5} \
    ${LA_EXTRA_SERVICES_LIST_GEN5} \
"

# multi-gvm is not yet supported on SA8797P, 8797-multi is used as a placeholder to mask off *-vm3.service
SYSTEMD_SERVICE:${PN}:append:gen5-multi = "\
    ${LA_EXTRA_SERVICES_LIST_GEN4_5} \
    ${LA_EXTRA_SERVICES_LIST_GEN5} \
    ${LV_SERVICES_LIST} \
    ${LV_EXTRA_SERVICES_LIST_GEN5} \
"

DEPENDS += "virtual/kernel-headers"
DEPENDS += "vmm-lib"
DEPENDS += "${@bb.utils.contains("MACHINE_FEATURES", "qti-umd", "msmhab", "", d)}"
DEPENDS += "systemd"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/vhost-user/.git;protocol=${PROTO};destsuffix=vhost-user-q;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/vhost-user"

inherit cmake systemd pkgconfig

CFLAGS += "\
    -I${STAGING_DIR_TARGET}/usr/include/${PREFERRED_PROVIDER_virtual/kernel} \
    --sysroot=${STAGING_DIR_TARGET} \
    -DCONFIG_HGY_PLATFORM \
    -D__linux__ \
"
TARGET_CC_ARCH += "${LDFLAGS}"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system/

    for service in ${LA_BASIC_SERVICES_LIST}; do
        install -m 0644 ${S}/${service} -D ${D}${systemd_unitdir}/system/
    done
}

do_install:append:sa7255-ivi() {
    for service in ${LV_SERVICES_LIST}; do
        install -m 0644 ${S}/${service} -D ${D}${systemd_unitdir}/system/
    done
}

do_install:append:sa8255-ivi() {
    for service in ${LA_EXTRA_SERVICES_LIST_GEN4_5}; do
        install -m 0644 ${S}/${service} -D ${D}${systemd_unitdir}/system/
    done

    for service in ${LV_SERVICES_LIST}; do
        install -m 0644 ${S}/${service} -D ${D}${systemd_unitdir}/system/
    done
}

do_install:append:sa8775-flex() {
    for service in ${LA_EXTRA_SERVICES_LIST_GEN4_5}; do
        install -m 0644 ${S}/${service} -D ${D}${systemd_unitdir}/system/
    done

    for service in ${LV_SERVICES_LIST}; do
        install -m 0644 ${S}/${service} -D ${D}${systemd_unitdir}/system/
    done
}

do_install:append:gen5() {
    for service in ${LA_EXTRA_SERVICES_LIST_GEN4_5}; do
        install -m 0644 ${S}/${service} -D ${D}${systemd_unitdir}/system/
    done

    for service in ${LA_EXTRA_SERVICES_LIST_GEN5}; do
        install -m 0644 ${S}/${service} -D ${D}${systemd_unitdir}/system/
    done
}

# multi-gvm is not yet supported on SA8797P, 8797-multi is used as a placeholder to mask off *-vm3.service
do_install:append:gen5-multi() {
    for service in ${LA_EXTRA_SERVICES_LIST_GEN4_5}; do
        install -m 0644 ${S}/${service} -D ${D}${systemd_unitdir}/system/
    done

    for service in ${LA_EXTRA_SERVICES_LIST_GEN5}; do
        install -m 0644 ${S}/${service} -D ${D}${systemd_unitdir}/system/
    done

    for service in ${LV_SERVICES_LIST}; do
        install -m 0644 ${S}/${service} -D ${D}${systemd_unitdir}/system/
    done

    for service in ${LV_EXTRA_SERVICES_LIST_GEN5}; do
        install -m 0644 ${S}/${service} -D ${D}${systemd_unitdir}/system/
    done
}
