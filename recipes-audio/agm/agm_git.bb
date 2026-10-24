SUMMARY = "Audio Graph Manager"
DESCRIPTION = "This is the audio graph manager (AGM) service, it provides interfaces to allow mixer control and pcm plugins to interact and enable various audio usecases."
HOMEPAGE = "http://git.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
# Downstream product configuration may set this to "0" for legacy syslog.
AGM_AUDIO_LOG_UTIL_BRIDGE ?= "1"

DEPENDS += "tinyalsa-new tinycompress expat dbus \
    glib-2.0 spf gsl ats \
    audioreach-conf alsa-lib\
"
DEPENDS:append = " ${@bb.utils.contains('AGM_AUDIO_LOG_UTIL_BRIDGE', '1', 'audio-log-util', '', d)}"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/agm/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/agm;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/agm"

do_install:append () {
    install -m 0644 ${S}/ipc/DBus/config/agm-dbus.conf -D ${D}${sysconfdir}/dbus-1/system.d/agm-dbus.conf
}

do_install:append:gen5() {
    install -d ${D}${libdir}/ar
    install -m 0755 ${B}/snd_parser/.libs/libsndcardparser.so ${D}${libdir}/ar/
}

RM_WORK_EXCLUDE += "${PN}"

inherit autotools pkgconfig

EXTRA_OECONF += "\
    --with-glib --enable-alsalib\
"
EXTRA_OECONF += "--with-syslog=yes --with-agm_log_debug_enable=no --with-dbus=yes \
    --with-no_tinycompress=yes --with-use_default_acdb_path=yes --with-automotive=yes \
"
EXTRA_OECONF:append = " --with-audio-log-util-bridge=${@bb.utils.contains('AGM_AUDIO_LOG_UTIL_BRIDGE', '1', 'yes', 'no', d)}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

RDEPENDS:${PN} += "\
    alsa-lib \
    ar2-acdbdata \
    ar-util \
    glink-service-lrm \
    gsl \
"
RDEPENDS:${PN}:append = " ${@bb.utils.contains('AGM_AUDIO_LOG_UTIL_BRIDGE', '1', 'audio-log-util', '', d)}"

DEBUG_BUILD = "1"
PACKAGE_DEBUG_SPLIT_STYLE = "debug-file-directory"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""

# Include custom libdir and pkgconfig files in the main and -dev packages
FILES:${PN} += "${libdir}/ar/*.so"
FILES:${PN}-dev += "${libdir}/ar/pkgconfig ${libdir}/ar/pkgconfig/*.pc"
FILES:${PN} += "${libdir}/alsa-lib/*"
