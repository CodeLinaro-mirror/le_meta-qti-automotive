SUMMARY = "Android Binder IPC Support"
DESCRIPTION = "Intergrate binder daemon and configure proper binder device for android/android-like(starfish) system or module"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"
DEPENDS += "glib-2.0 libcutils libhardware liblog system-core"

SRC_URI = "\
    ${PATH_TO_REPO}/frameworks/.git;protocol=${PROTO};destsuffix=frameworks;usehead=1 \
    file://servicemanager.service \
    file://vndservicemanager.service \
    file://create-binder.sh \
    file://create-binder.service \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/frameworks/binder"

inherit autotools pkgconfig systemd useradd

PACKAGECONFIG ?= "\
                   ${@bb.utils.filter('DISTRO_FEATURES', 'selinux', d)} \
                   ${@bb.utils.filter('DISTRO_FEATURES', 'qti-vndbinder', d)} \
                   "
PACKAGECONFIG[selinux] = "--enable-selinux,--disable-selinux,libselinux"
PACKAGECONFIG[qti-vndbinder] = "--enable-vendor-binder,--disable-vendor-binder"

SYSTEMD_SERVICE:${PN} = "servicemanager.service create-binder.service"
SYSTEMD_SERVICE:${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'qti-vndbinder', 'vndservicemanager.service', '', d)}"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

# servicemanager.service and create-binder.service run as binder user
USERADD_PACKAGES = "${PN}"

GROUPADD_PARAM:${PN} = "binder"
USERADD_PARAM:${PN} = "--no-create-home -g binder --shell /bin/false binder"

EXTRA_OECONF += "--with-glib"
# This recipe assumes kernel always compile for default arch even when
# multilib compilation is enabled. If kernel is 64bit and binder is compiled
# for 32bit due to multilib settings default 64bit IPC need to be supported
# as kernel is 64bit. Only when kernel is 32bit, 32bit IPC need to be enabled.
EXTRA_OECONF:append:arm = " \
    ${@bb.utils.contains('MULTILIB_VARIANTS', 'lib32','','--enable-32bit-binder-ipc',d)} \
"

CFLAGS += "-I${STAGING_INCDIR}/libselinux"

do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d ${D}/${sysconfdir}/initscripts/
        install -m 0755 ${WORKDIR}/create-binder.sh -D ${D}${sysconfdir}/initscripts/create-binder
        install -d ${D}${systemd_unitdir}/system/
        install -m 0644 ${WORKDIR}/servicemanager.service -D ${D}${systemd_unitdir}/system/servicemanager.service
        install -m 0644 ${WORKDIR}/create-binder.service -D ${D}${systemd_unitdir}/system/create-binder.service
        if ${@bb.utils.contains('DISTRO_FEATURES', 'qti-vndbinder', 'true', 'false', d)}; then
            install -m 0644 ${WORKDIR}/vndservicemanager.service -D ${D}${systemd_unitdir}/system/vndservicemanager.service
        fi
        if ${@bb.utils.contains('DISTRO_FEATURES', 'smack', 'true', 'false', d)}; then
            #Add capabilities for create-binder.service to set smack lable
            sed -i "/^CapabilityBoundingSet/s/$/ CAP_MAC_ADMIN CAP_MAC_OVERRIDE/" ${D}${systemd_unitdir}/system/create-binder.service
            sed -i "/^AmbientCapabilities/s/$/ CAP_MAC_ADMIN CAP_MAC_OVERRIDE/" ${D}${systemd_unitdir}/system/create-binder.service
            #change binderfs smack label
            sed -i "30i\        chsmack -a '*' -t -r /dev/binderfs" ${D}${sysconfdir}/initscripts/create-binder
        fi
    fi
}

PACKAGE_BEFORE_PN = "${PN}-test"
FILES:${PN}-test = "${bindir}/test_binder"
