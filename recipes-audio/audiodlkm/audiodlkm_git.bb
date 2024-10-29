SUMMARY = "Audio Drivers Kernel Modules"
DESCRIPTION = "This is the audio driver based on ASoC architecture, used to communicate with DSP."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=689b0a45875711dc09b94e4b6524c3cd"
DEPENDS += "${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '5.15', "audio-devicetree", "", d)}"
DEPENDS += "virtual/kernel"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/audio-kernel/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/audio-kernel;usehead=1"
SRC_URI:append = " file://audio_load.conf"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/vendor/qcom/opensource/audio-kernel"

EXTRA_OEMAKE += "TARGET_SUPPORT=${@bb.utils.contains('BASEMACHINE', 'sa81x5', 'sa8155', '${BASEMACHINE}', d)}"
EXTRA_OEMAKE += "AR_SUPPORT=${@bb.utils.contains('MACHINE_FEATURES', 'qti-audio-ar', 'yes', 'no', d)}"

MODULES = "\
        dsp/codecs/native_dlkm.ko  \
        dsp/adsp_loader_dlkm.ko  \
        dsp/q6_notifier_dlkm.ko \
        dsp/q6_dlkm.ko \
        ipc/apr_dlkm.ko \
        asoc/codecs/hdmi_dlkm.ko \
        asoc/codecs/stub_dlkm.ko \
        asoc/platform_dlkm.ko \
        asoc/machine_dlkm.ko \
        soc/snd_event_dlkm.ko \
"
TECHPACK_MODULE_OUT = "${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '5.15', "${WORKDIR}/audio-kernel", "", d)}"
TECHPACK_MODULES = "${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '5.15', "${MODULES}", "", d)}"
TECHPACK_HEADERS = "${S}/include/uapi/audio"
TECHPACK_HEADERS_OUT = "audio-kernel/audio"
TECHPACK_MAKE_ARGS = "${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '5.15', "${EXTRA_OEMAKE} QTI_TECHPACK=true", "", d)} LEGACY_PATH="${S}""

inherit ${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '5.15', "qti-techpack", "module module-sign qperf qti-kernel-arch-clang", d)}

do_configure() {
    if ${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '5.4', 'true', 'false', d)}; then
        cp -f ${WORKDIR}/vendor/qcom/opensource/audio-kernel/Makefile.am ${WORKDIR}/vendor/qcom/opensource/audio-kernel/Makefile
    fi
}

do_install:append() {

    if ${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '5.4', 'true', 'false', d)}; then
        install -d -p ${D}${includedir}/audio-kernel/audio/linux
        install -d -p ${D}${includedir}/audio-kernel/audio/linux/mfd/wcd9xxx
        install -d -p ${D}${includedir}/audio-kernel/audio/sound

        process_headers "${S}/include/uapi/audio/linux" "${D}${includedir}/audio-kernel/audio/linux"
        process_headers "${S}/include/uapi/audio/linux/mfd/wcd9xxx" "${D}${includedir}/audio-kernel/audi
o/linux/mfd/wcd9xxx"
        process_headers "${S}/include/uapi/audio/sound" "${D}${includedir}/audio-kernel/audio/sound"
    fi
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -m 0755 ${WORKDIR}/audio_load.conf -D ${D}${sysconfdir}/modules-load.d/audio_load.conf
    else
        install -m 0755 ${WORKDIR}/audio_load.conf -D ${D}${sysconfdir}/modules/audio_load.conf
    fi

    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra

    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-audio-ar', 'true', 'false', d)}; then
        mv ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/asoc/gvm_spf_machine_dlkm.ko ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
        mv ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/asoc/codecs/stub_dlkm.ko ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
    else
        if ${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '5.15', 'false', 'true', d)}; then
            for i in $(find ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/. -name "*.ko"); do
                mv ${i} ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
            done
        fi
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
FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*"

# The inherit of module.bbclass will automatically name module packages with
# kernel-module-" prefix as required by the oe-core build environment. Also it
# replaces '_' with '-' in the module name.
RPROVIDES:${PN} += "${@'kernel-module-adsp-loader-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-apr-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-bolero-cdc-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-csra66x0-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-va-macro-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wsa-macro-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-cpe-lsm-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-machine-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-native-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-pinctrl-lpi-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-pinctrl-wcd-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-platform-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-q6-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-q6-notifier-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-q6-pdr-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-swr-ctrl-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-swr-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-usf-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wglink-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-analog-cdc-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-digital-cdc-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-msm-sdw-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wcd934x-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-hdmi-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-ep92-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-machine-ext-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-machine-ext-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-mbhc-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-stub-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wcd-core-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wcd-cpe-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wcd9335-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wcd9xxx-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wsa881x-analog-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wsa881x-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wcd-spi-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-machine-int-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-snd-event-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-rx-macro-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-tx-macro-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wcd937x-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wcd937x-slave-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-gvm-spf-machine-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"

KERNEL_CC += "-Wno-error=maybe-uninitialized"
