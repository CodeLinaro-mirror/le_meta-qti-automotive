SUMMARY = "Early_init bash script for early_init feature"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "file://early_init.c"
SRC_URI += "file://early_init.conf"
SRC_URI += "file://early_init_eth.conf"
SRC_URI += "file://early_init_noapp.conf"
SRC_URI += "file://preload1.sh"
SRC_URI += "file://preload2.sh"
SRC_URI += "file://preload3.sh"
SRC_URI += "file://preload4.sh"
SRC_URI += "file://preload5.sh"
SRC_URI += "file://preload6.sh"
SRC_URI += "file://preload7.sh"
SRC_URI += "file://preload8.sh"
SRC_URI += "file://preload9.sh"
SRC_URI += "file://preload10.sh"
SRC_URI += "file://preload11.sh"
SRC_URI += "file://preload12.sh"
SRC_URI += "file://preload13.sh"
SRC_URI += "file://preload14.sh"
SRC_URI += "file://preload15.sh"
SRC_URI += "file://preload16.sh"

SECURITY_CFLAGS:pn-${PN} = "${SECURITY_NOPIE_CFLAGS}"
S = "${WORKDIR}"

do_compile() {
        if ${@bb.utils.contains('DISTRO_FEATURES', 'early-ethernet', 'true', 'false', d)}; then
            ${CC} ${CFLAGS} ${LDFLAGS} -static -D EARLY_ETHERNET -D WESTON_USER=${WESTONUSER} -o ${S}/early_init ${S}/early_init.c
        else
            ${CC} ${CFLAGS} ${LDFLAGS} -static -o ${S}/early_init ${S}/early_init.c
        fi
}
do_install() {
    # Add early_init script for early_init feature
    install -d ${D}${sbindir}
    install -m 0755 ${S}/early_init  ${D}${sbindir}/early_init
    install -d ${D}${sysconfdir}
    if ${@bb.utils.contains('DISTRO_FEATURES', 'early_init', 'true', 'false', d)} || ${@bb.utils.contains('DISTRO_FEATURES', 'early-ethernet', 'true', 'false', d)}; then
        if ${@bb.utils.contains('DISTRO_FEATURES', 'early-ethernet', 'true', 'false', d)}; then
            install -m 0644 ${S}/early_init_eth.conf  ${D}${sysconfdir}/early_init.conf
        else
            if ${@bb.utils.contains('DISTRO_FEATURES', 'early_userspace', 'true', 'false', d)}; then
                install -m 0644 ${S}/early_init.conf  ${D}${sysconfdir}/early_init.conf
            else
                install -m 0644 ${S}/early_init_noapp.conf  ${D}${sysconfdir}/early_init.conf
                install -m 0755 ${S}/preload1.sh -D ${D}${sbindir}/preload1.sh
                install -m 0755 ${S}/preload2.sh -D ${D}${sbindir}/preload2.sh
                install -m 0755 ${S}/preload3.sh -D ${D}${sbindir}/preload3.sh
                install -m 0755 ${S}/preload4.sh -D ${D}${sbindir}/preload4.sh
                install -m 0755 ${S}/preload5.sh -D ${D}${sbindir}/preload5.sh
                install -m 0755 ${S}/preload6.sh -D ${D}${sbindir}/preload6.sh
                install -m 0755 ${S}/preload7.sh -D ${D}${sbindir}/preload7.sh
                install -m 0755 ${S}/preload8.sh -D ${D}${sbindir}/preload8.sh
                install -m 0755 ${S}/preload9.sh -D ${D}${sbindir}/preload9.sh
                install -m 0755 ${S}/preload10.sh -D ${D}${sbindir}/preload10.sh
                install -m 0755 ${S}/preload11.sh -D ${D}${sbindir}/preload11.sh
                install -m 0755 ${S}/preload12.sh -D ${D}${sbindir}/preload12.sh
                install -m 0755 ${S}/preload13.sh -D ${D}${sbindir}/preload13.sh
                install -m 0755 ${S}/preload14.sh -D ${D}${sbindir}/preload14.sh
                install -m 0755 ${S}/preload15.sh -D ${D}${sbindir}/preload15.sh
                install -m 0755 ${S}/preload16.sh -D ${D}${sbindir}/preload16.sh
            fi
        fi
    fi
}
