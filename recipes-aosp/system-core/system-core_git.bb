SUMMARY = "Android core components"
DESCRIPTION = "The system/core directory is intended for pieces of the world \
that are the core of the embedded linux platform at the heart of Android. \
These essential bits are required for basic booting, operation, and debugging."
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=c1a3ff0b97f199c7ebcfdd4d3fed238e"

DEPENDS += "ext4-utils glib-2.0 libbase libcutils libmincrypt libutils virtual/kernel-headers openssl system-core-adbd"

PR = "r19"

SRC_URI = "${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=system/core;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/system/core"

inherit autotools pkgconfig systemd useradd

COMPOSITION = "901D"

SYSTEMD_PACKAGES = "${PN}-dlkm"
SYSTEMD_SERVICE:${PN}-dlkm = "dlkm.service"

USERADD_PACKAGES = "${PN}-leprop"

GROUPADD_PARAM:${PN}-leprop = "leprop"
USERADD_PARAM:${PN}-leprop = "-g leprop --no-create-home --shell /bin/false leprop"

CPPFLAGS += "-I${STAGING_INCDIR}/ext4_utils"

EXTRA_OECONF = "\
    --with-host-os=${HOST_OS} \
    --with-glib \
    --with-sanitized-headers=${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel} \
    --disable-debuggerd \
    --disable-libsync \
"

do_install:append() {
    install -d ${D}${sysconfdir}/usb/
    install -b -m 0644 /dev/null ${D}${sysconfdir}/usb/boot_hsusb_comp
    install -b -m 0644 /dev/null ${D}${sysconfdir}/usb/boot_hsic_comp
    echo ${COMPOSITION} > ${D}${sysconfdir}/usb/boot_hsusb_comp

    install -d ${D}${base_sbindir}
    install -m 0755 ${S}/usb/usb_composition -D ${D}${base_sbindir}/
    install -d ${D}${base_sbindir}/usb/compositions/
    install -m 0755 ${S}/usb/compositions/* -D ${D}${base_sbindir}/usb/compositions/
    install -m 0755 ${S}/usb/target -D ${D}${base_sbindir}/usb/

    install -d ${D}${base_sbindir}/usb/debuger/
    install -m 0755 ${S}/usb/debuger/debugFiles -D ${D}${base_sbindir}/usb/debuger/
    install -m 0755 ${S}/usb/debuger/help -D ${D}${base_sbindir}/usb/debuger/
    install -m 0755 ${S}/usb/debuger/usb_debug -D ${D}${base_sbindir}/

    install -b -m 0644 /dev/null -D ${D}${sysconfdir}/build.prop
    chown leprop:leprop ${D}${sysconfdir}/build.prop

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -m 0755 ${S}/usb/start_usb -D ${D}${sysconfdir}/initscripts/usb
        install -m 0750 ${S}/rootdir/etc/dlkm.sh -D ${D}${sysconfdir}/initscripts/dlkm
        install -m 0750 ${S}/rootdir/etc/init.qcom.post_boot.sh -D ${D}${sysconfdir}/initscripts/init_post_boot
        install -m 0750 ${S}/rootdir/etc/init.qti.early_boot.sh -D ${D}${sysconfdir}/initscripts/init_early_boot

        install -d ${D}${systemd_unitdir}/system/
        install -d ${D}${systemd_unitdir}/system/multi-user.target.wants/
        install -d ${D}${systemd_unitdir}/system/sysinit.target.wants/

        install -m 0644 ${S}/usb/usb.service -D ${D}${systemd_unitdir}/system/usb.service
        ln -sf ${systemd_unitdir}/system/usb.service ${D}${systemd_unitdir}/system/multi-user.target.wants/usb.service

        install -m 0644 ${S}/rootdir/etc/dlkm.service -D ${D}${systemd_unitdir}/system/dlkm.service

        install -m 0644 ${S}/rootdir/etc/init_post_boot.service -D ${D}${systemd_unitdir}/system/init_post_boot.service
        ln -sf ${systemd_unitdir}/system/init_post_boot.service \
            ${D}${systemd_unitdir}/system/multi-user.target.wants/init_post_boot.service

        install -m 0644 ${S}/rootdir/etc/init_early_boot.service -D ${D}${systemd_unitdir}/system/init_early_boot.service
        ln -sf ${systemd_unitdir}/system/init_early_boot.service \
            ${D}${systemd_unitdir}/system/sysinit.target.wants/init_early_boot.service

        install -m 0644 ${S}/leproperties/leprop.service -D ${D}${systemd_unitdir}/system/leprop.service
        ln -sf ${systemd_unitdir}/system/leprop.service \
            ${D}${systemd_unitdir}/system/multi-user.target.wants/leprop.service

        # update usb.service to depend on var-volatile.mount
        sed -i -e '/^After/d' ${D}${systemd_unitdir}/system/usb.service
        sed -i -e '/^Requires/d' ${D}${systemd_unitdir}/system/usb.service
        sed -i -e '/^Descr/a\RequiresMountsFor=\/var' ${D}${systemd_unitdir}/system/usb.service
        if ${@bb.utils.contains('TCMODE', 'external-ubuntu', 'false', 'true', d)}; then
            sed -i -e '/^Descr/a\Requires=var-usb.service' ${D}${systemd_unitdir}/system/usb.service
        fi
        sed -i -e '/^Descr/a\After=var-volatile.mount leprop.service systemd-modules-load.service' ${D}${systemd_unitdir}/system/usb.service
        sed -i -e '/^ExecStartPre/d' ${D}${systemd_unitdir}/system/usb.service
        sed -i -e '/^Descr/a\ConditionVirtualization=!container' ${D}${systemd_unitdir}/system/usb.service
    fi

    # remove headers, use system-core-headers recipe
    rm -rf ${D}${includedir}
}

PACKAGES =+ "${PN}-usb ${PN}-dlkm ${PN}-post-boot ${PN}-early-boot ${PN}-leprop"

FILES:${PN}-usb += "\
    ${base_sbindir}/usb_composition \
    ${bindir}/usb_composition_switch \
    ${base_sbindir}/usb/compositions/* \
    ${sysconfdir}/usb/* \
    ${base_sbindir}/usb/* \
    ${base_sbindir}/usb_debug \
    ${base_sbindir}/usb/debuger/* \
    ${systemd_unitdir}/system/usb.service \
    ${systemd_unitdir}/system/multi-user.target.wants/usb.service \
    ${sysconfdir}/initscripts/usb \
"

FILES:${PN}-dlkm += "\
    ${systemd_unitdir}/system/dlkm.service \
    ${systemd_unitdir}/system/multi-user.target.wants/dlkm.service \
    ${sysconfdir}/initscripts/dlkm \
"

FILES:${PN}-post-boot += "\
    ${systemd_unitdir}/system/init_post_boot.service \
    ${systemd_unitdir}/system/multi-user.target.wants/init_post_boot.service \
    ${sysconfdir}/initscripts/init_post_boot \
"

FILES:${PN}-early-boot += "\
    ${systemd_unitdir}/system/init_early_boot.service \
    ${systemd_unitdir}/system/sysinit.target.wants/init_early_boot.service \
    ${sysconfdir}/initscripts/init_early_boot \
"

FILES:${PN}-leprop += "\
    ${sbindir}/leprop-service \
    ${bindir}/getprop \
    ${bindir}/setprop \
    ${systemd_unitdir}/system/leprop.service \
    ${systemd_unitdir}/system/multi-user.target.wants/leprop.service \
    ${sysconfdir}/build.prop \
"

ALLOW_EMPTY:${PN} = "1"
ALLOW_EMPTY:${PN}-dev = "1"
