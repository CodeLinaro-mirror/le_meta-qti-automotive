SUMMARY = "Vhost user qti binary"
DESCRIPTION = "vhost user qti binary with multimedia services"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

SYSTEMD_SERVICE:${PN} = "\
    vhost-user-disp.service \
    vhost-user-gpu.service \
    vhost-user-misc.service \
    vhost-user-aud.service \
    vhost-user-vid.service \
    vhost-user-cam.service \
    vhost-user-vnw.service \
    vhost-user-ext.service \
    vhost-user-gpce.service \
    vhost-user-soccp.service \
    vhost-user-dprx.service \
    vhost-user-eva.service \
"

# multi-gvm is not yet supported on SA8797P, 8797-multi is used as a placeholder to mask off *-vm3.service
SYSTEMD_SERVICE:${PN}:append:sa8797-multi = "\
    vhost-user-disp-vm3.service \
    vhost-user-gpu-vm3.service \
    vhost-user-misc-vm3.service \
    vhost-user-aud-vm3.service \
    vhost-user-vid-vm3.service \
    vhost-user-cam-vm3.service \
    vhost-user-vnw-vm3.service \
    vhost-user-ext-vm3.service \
    vhost-user-gpce-vm3.service \
    vhost-user-soccp-vm3.service \
    vhost-user-dprx-vm3.service \
    vhost-user-eva-vm3.service \
"

DEPENDS += "virtual/kernel-headers"
DEPENDS += "${@bb.utils.contains("MACHINE_FEATURES", "qti-umd", "msmhab", "", d)}"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/vhost-user/.git;protocol=${PROTO};destsuffix=vhost-user-q;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/vhost-user"

inherit cmake systemd

CFLAGS += "\
    -I${STAGING_DIR_TARGET}/usr/include/${PREFERRED_PROVIDER_virtual/kernel} \
    --sysroot=${STAGING_DIR_TARGET} \
    -DCONFIG_HGY_PLATFORM \
    -D__linux__ \
"
TARGET_CC_ARCH += "${LDFLAGS}"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/vhost-user-disp.service -D ${D}${systemd_unitdir}/system/vhost-user-disp.service
    install -m 0644 ${S}/vhost-user-gpu.service -D ${D}${systemd_unitdir}/system/vhost-user-gpu.service
    install -m 0644 ${S}/vhost-user-misc.service -D ${D}${systemd_unitdir}/system/vhost-user-misc.service
    install -m 0644 ${S}/vhost-user-aud.service -D ${D}${systemd_unitdir}/system/vhost-user-aud.service
    install -m 0644 ${S}/vhost-user-vid.service -D ${D}${systemd_unitdir}/system/vhost-user-vid.service
    install -m 0644 ${S}/vhost-user-cam.service -D ${D}${systemd_unitdir}/system/vhost-user-cam.service
    install -m 0644 ${S}/vhost-user-vnw.service -D ${D}${systemd_unitdir}/system/vhost-user-vnw.service
    install -m 0644 ${S}/vhost-user-ext.service -D ${D}${systemd_unitdir}/system/vhost-user-ext.service
    install -m 0644 ${S}/vhost-user-gpce.service -D ${D}${systemd_unitdir}/system/vhost-user-gpce.service
    install -m 0644 ${S}/vhost-user-soccp.service -D ${D}${systemd_unitdir}/system/vhost-user-soccp.service
    install -m 0644 ${S}/vhost-user-dprx.service -D ${D}${systemd_unitdir}/system/vhost-user-dprx.service
    install -m 0644 ${S}/vhost-user-eva.service -D ${D}${systemd_unitdir}/system/vhost-user-eva.service
}

# multi-gvm is not yet supported on SA8797P, 8797-multi is used as a placeholder to mask off *-vm3.service
do_install:append:sa8797-multi() {
    install -m 0644 ${S}/vhost-user-disp-vm3.service -D ${D}${systemd_unitdir}/system/vhost-user-disp-vm3.service
    install -m 0644 ${S}/vhost-user-gpu-vm3.service -D ${D}${systemd_unitdir}/system/vhost-user-gpu-vm3.service
    install -m 0644 ${S}/vhost-user-misc-vm3.service -D ${D}${systemd_unitdir}/system/vhost-user-misc-vm3.service
    install -m 0644 ${S}/vhost-user-aud-vm3.service -D ${D}${systemd_unitdir}/system/vhost-user-aud-vm3.service
    install -m 0644 ${S}/vhost-user-vid-vm3.service -D ${D}${systemd_unitdir}/system/vhost-user-vid-vm3.service
    install -m 0644 ${S}/vhost-user-cam-vm3.service -D ${D}${systemd_unitdir}/system/vhost-user-cam-vm3.service
    install -m 0644 ${S}/vhost-user-vnw-vm3.service -D ${D}${systemd_unitdir}/system/vhost-user-vnw-vm3.service
    install -m 0644 ${S}/vhost-user-ext-vm3.service -D ${D}${systemd_unitdir}/system/vhost-user-ext-vm3.service
    install -m 0644 ${S}/vhost-user-gpce-vm3.service -D ${D}${systemd_unitdir}/system/vhost-user-gpce-vm3.service
    install -m 0644 ${S}/vhost-user-soccp-vm3.service -D ${D}${systemd_unitdir}/system/vhost-user-soccp-vm3.service
    install -m 0644 ${S}/vhost-user-dprx-vm3.service -D ${D}${systemd_unitdir}/system/vhost-user-dprx-vm3.service
    install -m 0644 ${S}/vhost-user-eva-vm3.service -D ${D}${systemd_unitdir}/system/vhost-user-eva-vm3.service
}
