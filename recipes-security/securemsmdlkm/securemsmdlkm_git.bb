SUMMARY = "QTI securemsm drivers"
DESCRIPTION = "This is the security driver, used to communicate with TrustZone."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/securemsm-kernel/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/securemsm-kernel;usehead=1 \
          file://security_load.conf"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/securemsm-kernel"

TECHPACK_MODULE_OUT = "${WORKDIR}/securemsm-kernel-out"
TECHPACK_MODULES = "qseecom_dlkm.ko tz_log_dlkm.ko qrng_dlkm.ko smcinvoke_dlkm.ko hdcp_qseecom_dlkm.ko qcrypto-msm_dlkm.ko qce50_dlkm.ko qcedev-mod_dlkm.ko"
TECHPACK_MODULES:remove:quin-gvm-lemans = "qcedev-mod_dlkm.ko"
TECHPACK_MODULES:append:quin-gvm-lemans = " qcedev_fe_dlkm.ko"
TECHPACK_HEADERS = "${S}/include/uapi"

inherit qti-techpack

do_install:append() {
    install -m 0755 ${WORKDIR}/security_load.conf -D ${D}${sysconfdir}/modules-load.d/security_load.conf
    install -d ${D}${includedir}/linux
    if [ -e ${WORKDIR}/vendor/qcom/opensource/securemsm-kernel/include/linux/smcinvoke.h ]; then
        install -m 0644 ${WORKDIR}/vendor/qcom/opensource/securemsm-kernel/include/linux/smcinvoke.h ${D}${includedir}/linux
    fi
    if [ -e ${WORKDIR}/vendor/qcom/opensource/securemsm-kernel/linux/smcinvoke.h ]; then
        install -m 0644 ${WORKDIR}/vendor/qcom/opensource/securemsm-kernel/linux/smcinvoke.h ${D}${includedir}/linux
    fi
    if [ -e ${WORKDIR}/vendor/qcom/opensource/securemsm-kernel/linux/qcedev.h ]; then
        install -m 0644 ${WORKDIR}/vendor/qcom/opensource/securemsm-kernel/linux/qcedev.h ${D}${includedir}/linux
    fi
    if [ -e ${WORKDIR}/vendor/qcom/opensource/securemsm-kernel/linux/fips_status.h ]; then
        install -m 0644 ${WORKDIR}/vendor/qcom/opensource/securemsm-kernel/linux/fips_status.h ${D}${includedir}/linux
    fi
    if [ -e ${WORKDIR}/vendor/qcom/opensource/securemsm-kernel/crypto-qti/fips_status.h ]; then
        install -m 0644 ${WORKDIR}/vendor/qcom/opensource/securemsm-kernel/crypto-qti/fips_status.h ${D}${includedir}/linux
    fi
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
RPROVIDES:${PN}:remove:quin-gvm-lemans = "kernel-module-qcedev-mod-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN}:append:quin-gvm-lemans = " kernel-module-qcedev-fe-dlkm-${KERNEL_VERSION}"

FILES:${PN} += "${sysconfdir}/*"
FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*"
