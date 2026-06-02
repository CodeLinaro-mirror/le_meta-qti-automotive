SRC_URI:append = " file://0001-alsa_lib-add-mmap-munmap-and-channel_info-callback-f.patch"
SRC_URI:append = " ${@bb.utils.contains('MACHINE_FEATURES', 'qti-gvm', '', 'file://0001-alsa_lib-support-mmap-mode-for-ioplug.patch', d)}"

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:${THISDIR}/${BPN}-${PV}:"
