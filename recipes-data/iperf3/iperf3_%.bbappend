FILESEXTRAPATHS:append := ":${THISDIR}/files"

SRC_URI:append = " \
    file://0001-fix-sigfault-in-perf-err-exit.patch \
    file://0002-add-udp-gso-gro-support.patch \
"