FILESEXTRAPATHS:append := ":${THISDIR}/files"

SRC_URI:append = " \
    file://0001-fix-sigfault-in-perf-err-exit.patch \
    file://0002-add-udp-gso-gro-support.patch \
    file://0001-iperf3-fix-sp-use-after-free-crash-in-iperf_udp_recv.patch \
"
