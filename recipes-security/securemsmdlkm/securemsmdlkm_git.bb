SUMMARY = "QTI securemsm drivers"
DESCRIPTION = "This is the security driver, used to communicate with TrustZone."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/securemsm-kernel/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/securemsm-kernel;usehead=1 \
          file://security_load.conf \
          file://security_smci_load.conf "

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/securemsm-kernel"

inherit qti-techpack

EXT_MODULE = "vendor/qcom/opensource/securemsm-kernel"

TECHPACK_MODULE_OUT = "${WORKDIR}/securemsm-kernel-out"
TECHPACK_MODULES = "qseecom_dlkm.ko tz_log_dlkm.ko qrng_dlkm.ko smcinvoke_dlkm.ko hdcp_qseecom_dlkm.ko qcrypto-msm_dlkm.ko qce50_dlkm.ko qcedev-mod_dlkm.ko"
TECHPACK_MODULES:remove:gvm-gen4-5 = "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'qcedev-mod_dlkm.ko', '', d)}"
TECHPACK_MODULES:append:gvm-gen4-5 = "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', ' qcedev_fe_dlkm.ko', '', d)}"
TECHPACK_HEADERS = "${S}/include/uapi"
TECHPACK_MODULES:remove:gvm-gen5 = "qseecom_dlkm.ko"

# CONFIG_ARCH_LEMANS/MONACO_AUTO/NORD are needed by securemsm-kernel/Kbuild
# to set enable_qcedev_fe=y which gates obj-$(CONFIG_QCEDEV_FE).
# This is a real make variable check (not shell export), so ARCH CONFIGs
# must be passed on the command line for qcedev_fe_dlkm.ko to be compiled.
TECHPACK_MAKE_ARGS:append:gvm-gen4-5 = " CONFIG_ARCH_LEMANS=y CONFIG_ARCH_MONACO_AUTO=y"
TECHPACK_MAKE_ARGS:append:gvm-gen5 = " CONFIG_ARCH_NORD=y"

do_install:append() {
    install -d ${D}/uni/hgy/etc/modules-load.d
    install -m 0755 ${WORKDIR}/security_load.conf -D ${D}${sysconfdir}/modules-load.d/security_load.conf
    install -m 0755 ${WORKDIR}/security_smci_load.conf -D ${D}/uni/hgy/etc/modules-load.d/security_load.conf
    install -d ${D}${includedir}/linux
    install -m 0644 ${WORKDIR}/vendor/qcom/opensource/securemsm-kernel/include/linux/smcinvoke.h ${D}${includedir}/linux
    install -m 0644 ${WORKDIR}/vendor/qcom/opensource/securemsm-kernel/crypto-qti/fips_status.h ${D}${includedir}/linux
    install -d ${D}${includedir}/hdcp_qseecom
    install -m 0644 ${S}/Module.symvers ${D}${includedir}/hdcp_qseecom
}

RPROVIDES:${PN} += "kernel-module-qseecom-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-tz-log-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-qrng-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-smcinvoke-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-hdcp-qseecom-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-qcrypto-msm-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-qce50-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-qcedev-mod-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN}:remove:gvm-gen4-5 = "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'kernel-module-qcedev-mod-dlkm-${KERNEL_VERSION}', '', d)}"
RPROVIDES:${PN}:append:gvm-gen4-5 = "${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', ' kernel-module-qcedev-fe-dlkm-${KERNEL_VERSION}', '', d)}"

FILES:${PN} += "${sysconfdir}/*"
FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*"
FILES:${PN} += "/uni/hgy/etc/modules-load.d/*"

