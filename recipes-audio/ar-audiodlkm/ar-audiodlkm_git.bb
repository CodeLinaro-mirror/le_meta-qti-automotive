SUMMARY = "Audio Drivers Kernel Modules for AudioReach"
DESCRIPTION = "This is the AudioReach based audio driver based on ASoC architecture, used to communicate with DSP."
HOMEPAGE = "https://www.codelinaro.org/"
LICENSE = "GPL-2.0-only-WITH-Linux-syscall-note"
LIC_FILES_CHKSUM = "file://NOTICE;md5=53c09804050a00b1d27bd609c4e1fc5a"
DEPENDS += "audio-devicetree"
DEPENDS += "virtual/kernel"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/audio-kernel-ar/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/audio-kernel-ar;usehead=1 \
           file://audio_load.conf \
          "

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/audio-kernel-ar"

EXTRA_OEMAKE:sa81x5 += "TARGET_SUPPORT=sa8155"
EXTRA_OEMAKE:quin-gvm-gen4-2 += "TARGET_SUPPORT=no AUTO_GVM=yes"
EXTRA_OEMAKE:quin-gvm-lemans += "TARGET_SUPPORT=no AUTO_GVM=yes"
EXTRA_OEMAKE:qtiquingvm8295 += "TARGET_SUPPORT=no AUTO_GVM=yes"
EXTRA_OEMAKE:monaco += "TARGET_SUPPORT=monaco"
EXTRA_OEMAKE:quin-gvm-monaco += "TARGET_SUPPORT=no AUTO_GVM=yes"
EXTRA_OEMAKE:quin-gvm-gen4 += "TARGET_SUPPORT=no AUTO_GVM=yes"

MODULES = "\
        dsp/spf_core_dlkm.ko  \
        dsp/audpkt_ion_dlkm.ko  \
        dsp/audio_prm_dlkm.ko  \
        dsp/adsp_loader_dlkm.ko  \
        dsp/q6_notifier_dlkm.ko \
        dsp/q6_dlkm.ko \
        ipc/gpr_dlkm.ko \
        ipc/audio_pkt_dlkm.ko  \
        asoc/codecs/stub_dlkm.ko \
        asoc/codecs/wcd9xxx_dlkm.ko \
        asoc/platform_dlkm.ko \
        asoc/spf_machine_dlkm.ko \
        soc/snd_event_dlkm.ko \
        soc/pinctrl_lpi_dlkm.ko \
"

TECHPACK_MODULE_OUT = "${WORKDIR}/audio-kernel-ar"
TECHPACK_MODULES = "${MODULES}"
TECHPACK_HEADERS = "${S}/include/uapi"
TECHPACK_HEADERS_OUT = "audio-kernel-ar/audio"
TECHPACK_MAKE_ARGS = "${EXTRA_OEMAKE} QTI_TECHPACK=true KERNEL_SRC=${STAGING_KERNEL_DIR}"

inherit ${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '5.4', "module module-sign qperf qti-kernel-arch-clang", "qti-techpack", d)}

do_configure() {
    if ${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '5.4', 'true', 'false', d)}; then
        cp -f ${WORKDIR}/vendor/qcom/opensource/audio-kernel-ar/Makefile.am ${WORKDIR}/vendor/qcom/opensource/audio-kernel-ar/Makefile
    fi
}

do_install:append() {

    if ${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '5.4', 'true', 'false', d)}; then
        install -d -p ${D}${includedir}/audio-kernel-ar/audio/linux/mfd/wcd9xxx
        process_headers "${S}/include/uapi/audio/linux/mfd/wcd9xxx" "${D}${includedir}/audio-kernel-ar/audio/linux/mfd/wcd9xxx"
    fi

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -m 0755 ${WORKDIR}/audio_load.conf -D ${D}${sysconfdir}/modules-load.d/audio_load.conf
    else
        install -m 0755 ${WORKDIR}/audio_load.conf -D ${D}${sysconfdir}/modules/audio_load.conf
    fi

    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra
    if ${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '5.4', 'true', 'false', d)}; then
        for i in $(find ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/. -name "*.ko"); do
            mv ${i} ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
        done
    fi

    rm -fr ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/asoc
    rm -fr ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/dsp
    rm -fr ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/ipc
    rm -fr ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/soc
}

process_headers() {
    cd ${STAGING_KERNEL_BUILDDIR}
    for name in $(ls $1/*.h); do
        ${STAGING_KERNEL_DIR}/scripts/headers_install.sh $1/$(basename $name) $2/$(basename $name)
    done
}

# install subdirectories under ${sysconfdir}
FILES:${PN} += "${sysconfdir}/*"
FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"

# The inherit of module.bbclass will automatically name module packages with
# kernel-module-" prefix as required by the oe-core build environment. Also it
# replaces '_' with '-' in the module name.
RPROVIDES:${PN} += "${@'kernel-module-adsp-loader-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-audio-pkt-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@' kernel-module-audio-prm-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@' kernel-module-audpkt-ion-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@' kernel-module-gpr-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@' kernel-module-spf-machine-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@' kernel-module-platform-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@' kernel-module-q6-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@' kernel-module-q6-notifier-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@' kernel-module-snd-event-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@' kernel-module-spf-core-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@' kernel-module-stub-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@' kernel-module-pinctrl-lpi-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@' kernel-module-wcd9xxx-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"

KERNEL_CC += "-Wno-error=maybe-uninitialized"
