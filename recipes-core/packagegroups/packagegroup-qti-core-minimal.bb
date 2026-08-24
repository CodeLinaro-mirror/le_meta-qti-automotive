DESCRIPTION = "The minimal set of packages required to boot the system, and some baics tools"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-core-minimal \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    packagegroup-qti-core-boot \
    packagegroup-qti-core-commonlibs \
    packagegroup-machine-base \
    "

RDEPENDS:${PN} += "\
    kernel-modules \
    system-core-adbd \
    system-core-leprop \
    ${@bb.utils.contains("MACHINE_FEATURES", "qti-hypervisor", "", "system-core-disksymlink", d)} \
    system-core-post-boot \
    system-core-usb \
    system-prop \
    memory-hotplug \
    ${@bb.utils.contains("MACHINE_FEATURES", "qti-hypervisor", "system-core-mount-ab", "", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "qti-hypervisor", "binder", "", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "qti-gunyah", "modules-load-late", "" ,d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "qti-umd", "platform-config", "", d)} \
    ${@bb.utils.contains("DISTRO_FEATURES", "early_init", "early-init", "" ,d)} \
    ${@bb.utils.contains("COMBINED_FEATURES", "hibernation", "hibernation", "" ,d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "qti-dlkm", "system-core-dlkm", "", d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '', bb.utils.contains_any('PREFERRED_PROVIDER_virtual/kernel', 'linux-ark linux-qcom-custom linux-qcom-custom-rt', 'irqbalance', '', d), d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "qti-umd", "early-service-infra power-utils", "", d)} \
    "
RDEPENDS:${PN}:append:sa8775 = " ${@bb.utils.contains("MACHINE_FEATURES", "qti-umd", "notify-aop", "", d)}"
RDEPENDS:${PN}:append:sa7255 = " ${@bb.utils.contains("MACHINE_FEATURES", "qti-umd", "notify-aop", "", d)}"
RDEPENDS:${PN}:append:monaco = " reboot-daemon"
RDEPENDS:${PN}:append:gen5 = " unify-target"

# packagegroup-qti-core-minimal.bb
RDEPENDS:${PN}:append:qclinux-gvm-gen5 = " \
    kernel-module-arm-smmu \
    kernel-module-iommu-logger \
    kernel-module-qcom-iommu-util \
    kernel-module-debug-symbol \
    kernel-module-qcom-wdt-core \
    kernel-module-qcom-soc-wdt \
"
