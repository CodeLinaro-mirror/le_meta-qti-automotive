SUMMARY = "Memory Hotplug"
DESCRIPTION = "During linux booting, we only leave enough memory for booting \
and remove other memory to reduce memory init time, thus linux kernel boot time. \
After linux fully boots up, this memory hotplug service needs to return all the \
removed memory back to linux."
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "\
    file://memory-hotplug.sh \
    file://memory-hotplug.service \
"

inherit systemd

SYSTEMD_SERVICE:${PN} = "memory-hotplug.service"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -D -m 0755 ${WORKDIR}/memory-hotplug.sh ${D}${bindir}/memory-hotplug.sh
    install -D -m 0644 ${WORKDIR}/memory-hotplug.service ${D}${systemd_unitdir}/system/memory-hotplug.service
}
