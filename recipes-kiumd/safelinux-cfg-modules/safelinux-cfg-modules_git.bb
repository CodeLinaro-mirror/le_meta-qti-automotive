SUMMARY = "UMD Related Kernel Modules"
DESCRIPTION = "These are UMD drivers to support userspace multimedia."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "${@bb.utils.contains('PREFERRED_PROVIDER_virtual/kernel', 'linux-ark', 'safelinux-sec-modules','', d)}"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/safelinux-cfg-modules/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/safelinux-cfg-modules;usehead=1"
SRC_URI:append = " \
    ${@bb.utils.contains("PREFERRED_VERSION_linux-msm", "6.1", 'file://0001-safelinux-cfg-mdoules-fix-build-issue-for-msm-6.1.patch;patchdir=../', '', d)} \
    ${@bb.utils.contains("PREFERRED_VERSION_linux-msm", "6.1", 'file://Kbuild', '', d)} \
"

SRC_URI:append = " file://${UMD_LOAD_CONF}"

UMD_LOAD_CONF ?= "umd_load_sa8797.conf"
UMD_LOAD_CONF:sa8775 = "umd_load_sa8775.conf"
UMD_LOAD_CONF:sa7255 = "umd_load_sa8775.conf"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/safelinux-cfg-modules/safelinux-modules"

TECHPACK_MODULES = "apps_pinctrl.ko scm_user_intf.ko qcom_dload_mode.ko vfio_iommu_qcom.ko iommu_iova_map.ko kiumd.ko qcom_uscmi.ko kryo_arm64_edac.ko kiumd_kgsl.ko mhi_ep_net.ko profiler.ko arm-smmu-qcom-fusa.ko pinctrl_fusa.ko qcom_vdev.ko dmabuf_share.ko iommu_faults.ko"

inherit qti-techpack

TECHPACK_MODULES:append:gen5 = " vendor_uscmi.ko"
TECHPACK_MODULES:append:sa8775 = " qcom_ethqos_filter.ko qcom_l3_cache_config.ko iommu_faults.ko"
TECHPACK_MODULES:append:sa7255 = " qcom_ethqos_filter.ko qcom_l3_cache_config.ko iommu_faults.ko"

do_patch_more() {
    if ${@bb.utils.contains("PREFERRED_VERSION_linux-msm", "6.1", 'true', 'false', d)} ; then
        mv ${WORKDIR}/Kbuild ${WORKDIR}/vendor/qcom/opensource/safelinux-cfg-modules/safelinux-modules/
    fi
}
addtask patch_more after do_patch before do_compile

EXTRA_OEMAKE += "CONFIG_PROFILER=y"
TECHPACK_MAKE_ARGS = "${@bb.utils.contains('PREFERRED_PROVIDER_virtual/kernel', 'linux-ark', 'KBUILD_EXTRA_SYMBOLS=${STAGING_INCDIR}/safelinux-sec-modules/Module.symvers','', d)}"

do_install:append() {
    install -d ${D}${includedir}/linux
    install -d ${D}${includedir}/uapi/misc
    install -m 0644 ${WORKDIR}/vendor/qcom/opensource/safelinux-cfg-modules/safelinux-modules/include/linux/iommu_iova_map.h ${D}${includedir}/linux
    install -m 0644 ${WORKDIR}/vendor/qcom/opensource/safelinux-cfg-modules/safelinux-modules/include/linux/qtee_shmbridge.h ${D}${includedir}/linux
    install -m 0644 ${WORKDIR}/vendor/qcom/opensource/safelinux-cfg-modules/safelinux-modules/include/uapi/misc/qcom_vdev.h ${D}${includedir}/linux
    install -m 0644 ${WORKDIR}/vendor/qcom/opensource/safelinux-cfg-modules/safelinux-modules/include/uapi/misc/iommu_iova_map_user.h ${D}${includedir}/uapi/misc
    install -m 0644 ${WORKDIR}/vendor/qcom/opensource/safelinux-cfg-modules/safelinux-modules/include/uapi/misc/kiumd.h ${D}${includedir}/uapi/misc
    install -m 0644 ${WORKDIR}/vendor/qcom/opensource/safelinux-cfg-modules/safelinux-modules/include/uapi/misc/dmabuf_share.h ${D}${includedir}/uapi/misc
    install -m 0644 ${WORKDIR}/vendor/qcom/opensource/safelinux-cfg-modules/safelinux-modules/include/uapi/misc/scm_user_intf.h ${D}${includedir}/uapi/misc
    install -m 0644 ${WORKDIR}/vendor/qcom/opensource/safelinux-cfg-modules/safelinux-modules/include/uapi/misc/qcom_uscmi.h ${D}${includedir}/uapi/misc
    install -m 0644 ${WORKDIR}/vendor/qcom/opensource/safelinux-cfg-modules/safelinux-modules/include/uapi/misc/vendor_uscmi.h ${D}${includedir}/uapi/misc
    install -m 0644 ${WORKDIR}/vendor/qcom/opensource/safelinux-cfg-modules/safelinux-modules/include/uapi/misc/qcom_l3_cache_config.h ${D}${includedir}/uapi/misc
    install -m 0755 ${WORKDIR}/${UMD_LOAD_CONF} -D ${D}${sysconfdir}/modules-load.d/umd_load.conf
}

EXTRA_OECONF += "--disable-doc --disable-Werror"

RPROVIDES:${PN} += "kernel-module-apps-pinctrl-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-scm-user-intf-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-qcom-dload-mode-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-arm-smmu-qcom-fusa-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-pinctrl-fusa-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-vfio-iommu-qcom-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-iommu-iova-map-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-kiumd-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-dmabuf-share-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-iommu-faults-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-qcom-uscmi-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-kryo-arm64-edac-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-kiumd-kgsl-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-mhi-ep-net-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-profiler-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-qcom-vdev-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-vendor-uscmi-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-iommu-faults-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-qcom-ethqos-filter-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-qcom-l3-cache-config-${KERNEL_VERSION}"

FILES:${PN} += "${sysconfdir}/modules-load.d/*"
FILES:${PN} += "${nonarch_base_libdir}/modules/*"
