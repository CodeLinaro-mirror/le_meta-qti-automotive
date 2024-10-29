SUMMARY = "QTI Codec2.0 application suit"
DESCRIPTION = "It includes some applications for Codec2, like codec2-caps, which is used \
               for save codec caps of C2 store into file."
HOMEPAGE = "https://git.codelinaro.org/"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://codec2-caps.cc;beginline=1;endline=33;md5=e03e27f162f326211da62f6ed30d539e"

DEPENDS += "codec2"

SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-qti-oss/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-qti-oss;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/codec2-app"

inherit cmake systemd useradd

CODEC2_USER = "codec2"
CODEC2_GROUP = "${CODEC2_USER}"
USERADD_PACKAGES = "${PN}"

GROUPADD_PARAM:${PN} = "${CODEC2_GROUP}"
USERADD_PARAM:${PN} = "--no-create-home --gid ${CODEC2_GROUP} --shell /bin/false ${CODEC2_USER}"

EXTRA_OECMAKE += "-DSYSTEMD_UNITDIR=${systemd_system_unitdir}"

SYSTEMD_SERVICE:${PN} = "init_codec2.service"

pkg_postinst:${PN} () {
    if command -v systemd-tmpfiles >/dev/null; then
        systemd-tmpfiles --create --remove --clean ${sysconfdir}/tmpfiles.d/init_codec2.conf
    fi
}

RDEPENDS:${PN} += "media-codec2"

TOOLCHAIN = "clang"
