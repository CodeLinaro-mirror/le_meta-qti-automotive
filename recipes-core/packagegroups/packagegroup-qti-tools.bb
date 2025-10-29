SUMMARY = "QTI package group for test"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-tools \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    file \
    pciutils \
    usbutils \
    util-linux \
    libgpiod-tools \
    exfat-utils \
    sysstat \
    ebtables \
    nftables \
    atrace \
    sos \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'systemd-analyze systemd-bootchart', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'asan', 'gcc-sanitizers', '', d)}  \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'lttng-modules lttng-tools lttng-ust', '', d)} \
    ${@bb.utils.contains_any('PREFERRED_PROVIDER_virtual/kernel', 'linux-qcom-custom linux-qcom-custom-rt', 'lttng-modules lttng-tools lttng-ust', '', d)} \
    ${@bb.utils.contains_any('VARIANT', 'perf user', '', 'devmem2', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'cntvct-log rtla', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '', bb.utils.contains_any('PREFERRED_PROVIDER_virtual/kernel', 'linux-qcom-custom linux-qcom-custom-rt', 'evtest lsof', '', d), d)} \
    "

# systemd-analyze is included in ubuntu's systemd package
# systemd-bootchart does not build properly for ubuntu yet
RDEPENDS:${PN}:remove = "${@bb.utils.contains('TCMODE', 'external-ubuntu', 'systemd-analyze systemd-bootchart', '', d)}"
RDEPENDS:${PN}:remove = "${@bb.utils.contains('TCMODE', 'external-ubuntu', 'lttng-tools lttng-ust', '', d)}"

# headless need exclude lttng related modules
RDEPENDS:${PN}:remove = "${@bb.utils.contains('DISTRO_NAME', 'headless', 'lttng-modules lttng-tools lttng-ust', '', d)}"
