SUMMARY = "Script and service to populate system properties"
DESCRIPTION = "Android system properties are a global dictionary of string \
key/value pairs, used to share system-wide configuration information. The \
build.prop file contains pesisit system properties."

HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=b796c0007db682166a1721da80267bb2"

SRC_URI = "\
    file://persist-prop.sh \
    file://persist-prop.service \
    file://system.prop \
"

SYSTEMD_SERVICE:${PN} = "persist-prop.service"

inherit systemd useradd

USERADD_PACKAGES = "${PN}"

GROUPADD_PARAM:${PN} = "leprop"
USERADD_PARAM:${PN} = "-g leprop --no-create-home --shell /bin/false leprop"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -m 0644 ${WORKDIR}/system.prop -D ${D}/build.prop
    # Remove empty lines and lines starting with '#'
    sed -i -e 's/#.*$//' -e '/^$/d' ${D}/build.prop

    install -m 0755 ${WORKDIR}/persist-prop.sh -D ${D}${base_sbindir}/persist-prop.sh
    install -m 0644 ${WORKDIR}/persist-prop.service -D ${D}/${systemd_unitdir}/system/persist-prop.service
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILES:${PN} += "/build.prop"
