SUMMARY = "Library for sensor"
DESCRIPTION = "The library provides sensor related functionality and udev rules"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "glib-2.0"

SRC_URI = "\
    ${PATH_TO_REPO}/hardware/qcom/sensors/.git;protocol=${PROTO};destsuffix=hardware/qcom/sensors;usehead=1 \
    file://iio.sh \
    file://sensors.sh \
    file://61-iio.rules \
    file://61-sensor.rules \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/hardware/qcom/sensors"

inherit autotools-brokensep pkgconfig useradd

EXTRA_OECONF = "--with-glib"

do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d ${D}${sysconfdir}/udev/rules.d/
        install -m 0444 ${WORKDIR}/61-sensor.rules ${D}${sysconfdir}/udev/rules.d/61-sensor.rules
        install -m 0444 ${WORKDIR}/61-iio.rules ${D}${sysconfdir}/udev/rules.d/61-iio.rules
        install -d ${D}${sysconfdir}/udev/scripts/
        install -m 0555 ${WORKDIR}/sensors.sh ${D}${sysconfdir}/udev/scripts/sensors.sh
        install -m 0555 ${WORKDIR}/iio.sh ${D}${sysconfdir}/udev/scripts/iio.sh
    fi
}

USERADD_PACKAGES = "${PN}"

GROUPADD_PARAM:${PN} = "system; leprop; sensors; inet; diag;"
USERADD_PARAM:${PN} = "--no-create-home -g sensors -G diag,inet,system,leprop --shell /bin/false sensors"
