# mask QCLINUX origin kernel recipes, use automotive's in qclinux1.0
BBMASK:append = " meta-qcom"
BBMASK:append = " meta-qti-auto-kernel/recipes-products/images/qti-image-ark-boot.bbappend"
BBMASK:append = " meta-qti-bsp/dynamic-layers/openembedded-layer/recipes-devtools/rsyslog/rsyslog_%.bbappend"
BBMASK:append = " meta-qti-bsp/recipes-edgegateway/parser/parser_0.1.bb"
BBMASK:append = " meta-qti-bsp/recipes-bsp/ab-update/qcom-abctl_1.0.bb"
BBMASK:append = " meta-qti-bsp/recipes-bsp/ab-update/qcom-libgpt_1.0.bb"
BBMASK:append = " meta-qti-bsp/recipes-bsp/ss-restart"
BBMASK:append = " meta-qti-bsp/recipes-bsp/u-boot/u-boot-tools_2025.01.bb"
BBMASK:append = " meta-qti-bsp/recipes-connectivity/bluez5/"
BBMASK:append = " meta-qti-bsp/recipes-core/uutils-coreutils/uutils-coreutils_%.bbappend"
BBMASK:append = " meta-qti-bsp/recipes-kernel"
BBMASK:append = " meta-qti-bsp/recipes-firmware/firmware"
BBMASK:append = " meta-qti-bsp/recipes-multimedia/fastrpc/fastrpc_git.bbappend"
BBMASK:append = " meta-qti-bsp/recipes-support/pd-mapper/pd-mapper_git.bbappend"
BBMASK:append = " meta-qti-internal"
BBMASK:append = " meta-qti-realtime/recipes-core/packagegroups/packagegroup-qcom-initscripts.bbappend"
BBMASK:append = " meta-qti-realtime/recipes-kernel/images/linux-qcom-uki.bbappend"
BBMASK:append = " meta-qti-realtime/recipes-products/images/qcom-guestvm-image.bbappend"

# Added temporarily: the following MM modules not found SRC_URI in gvm-gen4-5
BBMASK:append:gvm-gen4-5 = " \
    meta-qti-automotive/recipes-display/display-la/display-la-headers.bb \
    meta-qti-automotive/recipes-display/displaydlkm/displaydlkm_git.bb \
    meta-qti-automotive/recipes-graphics/display-hal/display-hal-linux_git.bb \
    meta-qti-automotive/recipes-graphics/display-hal/display-hal-headers_git.bb \
    meta-qti-automotive-prop/recipes-graphics/display-noship/display-noship-linux_git.bb \
    meta-qti-automotive-prop/recipes-graphics/display-ship/display-ship-linux_git.bb \
    meta-qti-automotive-prop/recipes-graphics/display-ship/display-vendor-headers_git.bb \
    meta-qti-automotive-prop/recipes-graphics/adreno/adreno_0.1.bb \
"

# BBMASK drops some recipe that checksum file not found
BBMASK:append = " ${MISSING_RECIPE_SRCLIST}"

# List of recipes to be masked for sa8797dc
LEDC10_MASK_RECIPES += "\
                       meta-qti-bsp/recipes-kernel/perf/perf.bbappend \
                       meta-qti-automotive/recipes-security/ \
"

BBMASK:append:sa8797dc = " ${LEDC10_MASK_RECIPES} "

MISSING_RECIPE_SRCLIST:append:gen5 = " ${MISSING_RECIPE_SRCLIST_BASELINE_LY011}"
MISSING_RECIPE_SRCLIST:append:sa8775 = " ${MISSING_RECIPE_SRCLIST_BASELINE_LY020}"
MISSING_RECIPE_SRCLIST:append:sa7255 = " ${MISSING_RECIPE_SRCLIST_BASELINE_LY020}"
MISSING_RECIPE_SRCLIST:append:qclinux-gvm-gen5 = " ${MISSING_RECIPE_SRCLIST_BASELINE_LV053}"
MISSING_RECIPE_SRCLIST:append:gvm-gen4-5 = " ${MISSING_RECIPE_SRCLIST_BASELINE_LV052}"
MISSING_RECIPE_SRCLIST:append:gvm-gen5 = " ${MISSING_RECIPE_SRCLIST_BASELINE_LV052}"

# Missing recipes globally
MISSING_RECIPE_SRCLIST += "\
    meta-qti-automotive/recipes-bsp/abctl/abctl_1.0.bb \
    meta-qti-automotive/recipes-bsp/libgpt/libgpt_1.0.bb \
    meta-qti-automotive/recipes-camera/cameradlkm/cameradlkm_0.1.bb \
    meta-qti-automotive/recipes-camera/cameradlkm/cameradlkm_0.2.bb \
    meta-qti-automotive/recipes-core/reboot-daemon/reboot-daemon_2.0.bb \
    meta-qti-automotive/recipes-data/avb-utils/avb-utils_1.0.bb \
    meta-qti-automotive/recipes-data/dataeth-dlkm/dataeth-dlkm_git.bb \
    meta-qti-automotive/recipes-data/emac-dwc-eqos/emac-dwc-eqos_git.bb \
    meta-qti-automotive/recipes-multimedia/media/media_0.1.bb \
"

# Missing recipes in baseline LY.AU.0.1.1 (used by gen5)
MISSING_RECIPE_SRCLIST_BASELINE_LY011 += "\
    meta-qti-automotive/recipes-aosp/libdmabufheap/libdmabufheap_git.bb \
    meta-qti-automotive/recipes-audio/agl-audio-plugin/agl-audio-plugin_0.1.bb \
    meta-qti-automotive/recipes-audio/agl-audio-plugin/agl-audio-plugin_0.1.bbappend \
    meta-qti-automotive/recipes-audio/agm-alsa-plugin/agm-alsa-plugin_git.bb \
    meta-qti-automotive/recipes-audio/agm-sndparser/agm-sndparser_git.bb \
    meta-qti-automotive/recipes-audio/agm-test/agm-test_git.bb \
    meta-qti-automotive/recipes-audio/agm-tinyalsa-plugin/agm-tinyalsa-plugin_git.bb \
    meta-qti-automotive/recipes-audio/agm_client/agm-client_git.bb \
    meta-qti-automotive/recipes-audio/agm_server/agm-server_git.bb \
    meta-qti-automotive/recipes-audio/ar-audiodlkm/ar-audiodlkm_git.bb \
    meta-qti-automotive/recipes-audio/ar-pal/ar-pal_git.bb \
    meta-qti-automotive/recipes-audio/audiodlkm/audiodlkm_git.bb \
    meta-qti-automotive/recipes-audio/msm-virtio-snd/msm-virtio-snd.bb \
    meta-qti-automotive/recipes-audio/pal-control-plugin/pal-control-plugin_git.bb \
    meta-qti-automotive/recipes-audio/pulseaudio-module-acdb/pulseaudio-module-acdb_git.bb \
    meta-qti-automotive/recipes-audio/pulseaudio-module-codec-control/pulseaudio-module-codec-control-headers_git.bb \
    meta-qti-automotive/recipes-audio/pulseaudio-module-codec-control/pulseaudio-module-codec-control_git.bb \
    meta-qti-automotive/recipes-audio/pulseaudio/pulseaudio_17.0.bbappend \
    meta-qti-automotive/recipes-bsp/libsensors/libsensors_git.bb \
    meta-qti-automotive/recipes-bsp/platformdlkm/platformdlkm_git.bb \
    meta-qti-automotive/recipes-data/ptp-virtual/ptp-virtual_git.bb \
    meta-qti-automotive/recipes-display/display-devicetree/display-devicetree_1.0.bb \
    meta-qti-automotive/recipes-display/displaydlkm/msm-ext-display_git.bb \
    meta-qti-automotive/recipes-graphics/graphics-hgsl/graphics-hgsldlkm_git.bb \
    meta-qti-automotive/recipes-kernel/dtc/dtc-aosp.bb \
    meta-qti-automotive/recipes-kernel/edk2/edk2_5.0.bb \
    meta-qti-automotive/recipes-kernel/kernel-aosp-tools/kernel-aosp-tools-native.bb \
    meta-qti-automotive/recipes-kernel/linux-msm/linux-ack-headers_6.12.bb \
    meta-qti-automotive/recipes-kernel/linux-msm/linux-ack_6.12.bb \
    meta-qti-automotive/recipes-kernel/linux-msm/linux-msm-headers_6.12.bb \
    meta-qti-automotive/recipes-kernel/linux-msm/linux-msm_6.12.bb \
    meta-qti-automotive/recipes-kernel/linux-msm/qcom-devicetree_6.12.bb \
    meta-qti-automotive/recipes-kernel/linux-msm/qcom-dtc-native.bb \
    meta-qti-automotive/recipes-kernel/linux-msm/soc-modules_6.12.bb \
    meta-qti-automotive/recipes-multimedia/dsp-kernel/dsp-kernel_git.bb \
    meta-qti-automotive/recipes-multimedia/videodlkm/videodlkm_git.bb \
    meta-qti-automotive/recipes-products/images/qti-image-boot.bb \
    meta-qti-automotive/recipes-qcvirtio/ \
    meta-qti-automotive/recipes-security/enable-fde/enable-fde_1.0.bb \
    meta-qti-automotive/recipes-security/securemsm-devicetree/securemsm-devicetree.bb \
    meta-qti-automotive/recipes-security/securemsmdlkm/securemsmdlkm_git.bb \
    meta-qti-automotive/recipes-wlan/wlan/qcacld32-ll-genoa_git.bb \
    meta-qti-automotive/recipes-wlan/wlan/qcacld32-ll-hasting-cnss0_git.bb \
    meta-qti-automotive/recipes-wlan/wlan/qcacld32-ll-hasting-cnss2_git.bb \
    meta-qti-automotive/recipes-wlan/wlan/qcacld32-ll-hasting_git.bb \
    meta-qti-automotive/recipes-wlan/wlan/qcacld32-ll-rome_git.bb \
"

# Missing recipes in baseline LY.AU.0.2.0 (used by sa8775/sa7255)
MISSING_RECIPE_SRCLIST_BASELINE_LY020 += "\
    meta-qti-automotive/recipes-aosp/libdmabufheap/libdmabufheap_git.bb \
    meta-qti-automotive/recipes-audio/acdb/acdb_git.bb \
    meta-qti-automotive/recipes-audio/agl-audio-plugin/agl-audio-plugin_0.1.bb \
    meta-qti-automotive/recipes-audio/agl-audio-plugin/agl-audio-plugin_0.1.bbappend \
    meta-qti-automotive/recipes-audio/agm-alsa-plugin/agm-alsa-plugin_git.bb \
    meta-qti-automotive/recipes-audio/agm-sndparser/agm-sndparser_git.bb \
    meta-qti-automotive/recipes-audio/agm-test/agm-test_git.bb \
    meta-qti-automotive/recipes-audio/agm-tinyalsa-plugin/agm-tinyalsa-plugin_git.bb \
    meta-qti-automotive/recipes-audio/agm/agm_git.bb \
    meta-qti-automotive/recipes-audio/agm_client/agm-client_git.bb \
    meta-qti-automotive/recipes-audio/agm_server/agm-server_git.bb \
    meta-qti-automotive/recipes-audio/ar-audio-init-service/ar-audio-init-service_git.bb \
    meta-qti-automotive/recipes-audio/ar-audio-service-utils/ar-audio-service-utils_git.bb \
    meta-qti-automotive/recipes-audio/ar-audiodlkm/ar-audiodlkm_git.bb \
    meta-qti-automotive/recipes-audio/ar-dev-plugin/ar-dev-plugin_git.bb \
    meta-qti-automotive/recipes-audio/ar-osal/ar-osal_git.bb \
    meta-qti-automotive/recipes-audio/ar-pal/ar-pal_git.bb \
    meta-qti-automotive/recipes-audio/ar-scmi/ar-scmi_git.bb \
    meta-qti-automotive/recipes-audio/ar-util/ar-util_git.bb \
    meta-qti-automotive/recipes-audio/ar2-audio-service/ar2-audio-service_git.bb \
    meta-qti-automotive/recipes-audio/ats/ats_git.bb \
    meta-qti-automotive/recipes-audio/audio-device-manager/audio-device-manager_git.bb \
    meta-qti-automotive/recipes-audio/audio-plugin-intf/audio-plugin-intf_git.bb \
    meta-qti-automotive/recipes-audio/audio-utils/audio-utils_git.bb \
    meta-qti-automotive/recipes-audio/audiodlkm/audiodlkm_git.bb \
    meta-qti-automotive/recipes-audio/audioreach-conf/audioreach-conf_git.bb \
    meta-qti-automotive/recipes-audio/gpr/gpr_git.bb \
    meta-qti-automotive/recipes-audio/gsl/gsl_git.bb \
    meta-qti-automotive/recipes-audio/msm-virtio-snd/msm-virtio-snd.bb \
    meta-qti-automotive/recipes-audio/pal-control-plugin/pal-control-plugin_git.bb \
    meta-qti-automotive/recipes-audio/pulseaudio-module-acdb/pulseaudio-module-acdb_git.bb \
    meta-qti-automotive/recipes-audio/pulseaudio-module-codec-control/pulseaudio-module-codec-control-headers_git.bb \
    meta-qti-automotive/recipes-audio/pulseaudio-module-codec-control/pulseaudio-module-codec-control_git.bb \
    meta-qti-automotive/recipes-audio/pulseaudio/pulseaudio_17.0.bbappend \
    meta-qti-automotive/recipes-audio/sound-card-info-common-header/sound-card-info-common-header_git.bb \
    meta-qti-automotive/recipes-audio/sound-card-info-util/sound-card-info-util_git.bb \
    meta-qti-automotive/recipes-audio/spf/spf_git.bb \
    meta-qti-automotive/recipes-bsp/libsensors/libsensors_git.bb \
    meta-qti-automotive/recipes-bsp/platformdlkm/platformdlkm_git.bb \
    meta-qti-automotive/recipes-data/ptp-virtual/ptp-virtual_git.bb \
    meta-qti-automotive/recipes-display/display-devicetree/display-devicetree_1.0.bb \
    meta-qti-automotive/recipes-display/display-intf/display-intf-headers.bb \
    meta-qti-automotive/recipes-display/display-la/display-la-headers.bb \
    meta-qti-automotive/recipes-display/displaydlkm/msm-ext-display_git.bb \
    meta-qti-automotive/recipes-graphics/graphics-hgsl/graphics-hgsldlkm_git.bb \
    meta-qti-automotive/recipes-kernel/dtc/dtc-aosp.bb \
    meta-qti-automotive/recipes-kernel/edk2/edk2_5.0.bb \
    meta-qti-automotive/recipes-kernel/kernel-aosp-tools/kernel-aosp-tools-native.bb \
    meta-qti-automotive/recipes-kernel/linux-msm/linux-ack-headers_6.12.bb \
    meta-qti-automotive/recipes-kernel/linux-msm/linux-ack_6.12.bb \
    meta-qti-automotive/recipes-kernel/linux-msm/linux-msm-headers_6.12.bb \
    meta-qti-automotive/recipes-kernel/linux-msm/linux-msm_6.12.bb \
    meta-qti-automotive/recipes-kernel/linux-msm/qcom-devicetree_6.12.bb \
    meta-qti-automotive/recipes-kernel/linux-msm/qcom-dtc-native.bb \
    meta-qti-automotive/recipes-kernel/linux-msm/soc-modules_6.12.bb \
    meta-qti-automotive/recipes-multimedia/codec2/codec2-app_git.bb \
    meta-qti-automotive/recipes-multimedia/dsp-kernel/dsp-kernel_git.bb \
    meta-qti-automotive/recipes-multimedia/gstreamer/drm-player-example_1.0.0.bb \
    meta-qti-automotive/recipes-multimedia/gstreamer/gstreamer1.0-plugin-qvais_git.bb \
    meta-qti-automotive/recipes-multimedia/gstreamer/gstreamer1.0-plugins-codec2_1.0.0.bb \
    meta-qti-automotive/recipes-multimedia/gstreamer/gstreamer1.0-plugins-drmdecryptor_1.0.0.bb \
    meta-qti-automotive/recipes-multimedia/gstreamer/gstreamer1.0-plugins-extpoolsink.bb \
    meta-qti-automotive/recipes-multimedia/gstreamer/gstreamer1.0-plugins-qcarcamsrc_git.bb \
    meta-qti-automotive/recipes-multimedia/gstreamer/gstreamer1.0-plugins-qscreencapsrc_1.0.0.bb \
    meta-qti-automotive/recipes-multimedia/gstreamer/gstreamer1.0-plugins-qvblend_1.0.bb \
    meta-qti-automotive/recipes-multimedia/gstreamer/gstreamer1.0-plugins-qvconv.bb \
    meta-qti-automotive/recipes-multimedia/gstreamer/gstreamer1.0-plugins-qvdeinterlace_1.0.bb \
    meta-qti-automotive/recipes-multimedia/gstreamer/gstreamer1.0-plugins-qvrate_git.bb \
    meta-qti-automotive/recipes-multimedia/gstreamer/gstreamer1.0-plugins-vesdeliver_1.0.0.bb \
    meta-qti-automotive/recipes-multimedia/gstreamer/mm-vdec-omx-test-lite_0.1.bb \
    meta-qti-automotive/recipes-multimedia/gstreamer/mm-venc-omx-test-lite_0.1.bb \
    meta-qti-automotive/recipes-multimedia/gstreamer/secure-video-app_1.0.0.bb \
    meta-qti-automotive/recipes-multimedia/videodlkm/videodlkm_git.bb \
    meta-qti-automotive/recipes-products/images/qti-image-boot.bb \
    meta-qti-automotive/recipes-rideware/qcnode/qcnode_2.0.bb \
    meta-qti-automotive/recipes-security/enable-fde/enable-fde_1.0.bb \
    meta-qti-automotive/recipes-security/securemsm-devicetree/securemsm-devicetree.bb \
    meta-qti-automotive/recipes-security/securemsmdlkm/securemsmdlkm_git.bb \
"

# Missing recipes in baseline LV.AU.0.5.2 (used by gvm-gen4-5/gvm-gen5)
MISSING_RECIPE_SRCLIST_BASELINE_LV052 += "\
    meta-qti-automotive/recipes-audio/acdb/acdb_git.bb \
    meta-qti-automotive/recipes-audio/ar-audio-init-service/ar-audio-init-service_git.bb \
    meta-qti-automotive/recipes-audio/ar-audio-service-utils/ar-audio-service-utils_git.bb \
    meta-qti-automotive/recipes-audio/ar-dev-plugin/ar-dev-plugin_git.bb \
    meta-qti-automotive/recipes-audio/ar-osal/ar-osal_git.bb \
    meta-qti-automotive/recipes-audio/ar-scmi/ar-scmi_git.bb \
    meta-qti-automotive/recipes-audio/ar-util/ar-util_git.bb \
    meta-qti-automotive/recipes-audio/ar2-audio-service/ar2-audio-service_git.bb \
    meta-qti-automotive/recipes-audio/ats/ats_git.bb \
    meta-qti-automotive/recipes-audio/audio-device-manager/audio-device-manager_git.bb \
    meta-qti-automotive/recipes-audio/audio-plugin-intf/audio-plugin-intf_git.bb \
    meta-qti-automotive/recipes-audio/audio-utils/audio-utils_git.bb \
    meta-qti-automotive/recipes-audio/audiolite-dlkm/audiolite-devicetree_git.bb \
    meta-qti-automotive/recipes-audio/audiolite-dlkm/audiolite-dlkm_git.bb \
    meta-qti-automotive/recipes-audio/audioreach-conf/audioreach-conf_git.bb \
    meta-qti-automotive/recipes-audio/gpr/gpr_git.bb \
    meta-qti-automotive/recipes-audio/gsl/gsl_git.bb \
    meta-qti-automotive/recipes-audio/sound-card-info-common-header/sound-card-info-common-header_git.bb \
    meta-qti-automotive/recipes-audio/sound-card-info-util/sound-card-info-util_git.bb \
    meta-qti-automotive/recipes-audio/spf/spf_git.bb \
    meta-qti-automotive/recipes-core/bootkpi-logging/bootkpi-logging.bb \
    meta-qti-automotive/recipes-core/early-service-infra/early-service-infra_git.bb \
    meta-qti-automotive/recipes-core/irqbalance/irqbalance_git.bbappend \
    meta-qti-automotive/recipes-core/notify_aop/notify-aop_git.bb \
    meta-qti-automotive/recipes-core/platform-config/platform-config_git.bb \
    meta-qti-automotive/recipes-core/power-utils/power-utils_git.bb \
    meta-qti-automotive/recipes-data/aurix-can/aurix-can_git.bb \
    meta-qti-automotive/recipes-extra/recipes-core/scmi-test/scmi-test_1.0.bb \
    meta-qti-automotive/recipes-graphics/auto-gfx-app/auto-gfx-app.bb \
    meta-qti-automotive/recipes-graphics/mm-auto-ksync/ksyncdlkm_git.bb \
    meta-qti-automotive/recipes-kernel/edk2/edk2_git.bb \
    meta-qti-automotive/recipes-kernel/oot-dtbo/oot-dtbo.bb \
    meta-qti-automotive/recipes-kiumd/dspfirmware-mount/dspfirmware-mount_git.bb \
    meta-qti-automotive/recipes-kiumd/gvm-net-config/gvm-net-config_git.bb \
    meta-qti-automotive/recipes-kiumd/kiumd-headers/kiumd-headers_git.bb \
    meta-qti-automotive/recipes-kiumd/safelinux-cfg-modules/safelinux-cfg-modules_git.bb \
    meta-qti-automotive/recipes-qcvirtio \
    meta-qti-automotive/recipes-rideware/qcnode/qcnode_2.0.bb \
    meta-qti-automotive/recipes-safelinux-dbg-modules/safelinux-dbg-modules/safelinux-dbg-modules_git.bb \
    meta-qti-automotive/recipes-security/security-modules/safelinux-sec-modules_git.bb \
    meta-qti-automotive/recipes-virt/gunyah-drivers/gunyah-drivers_git.bb \
    meta-qti-automotive/recipes-virt/hyp-udmabuf/hyp-udmabuf-test_git.bb \
    meta-qti-automotive/recipes-virt/hyp-udmabuf/hyp-udmabuf_git.bb \
    meta-qti-automotive/recipes-virt/msmhab/msmhab_git.bb \
    meta-qti-automotive/recipes-virt/qcrosvm/qcrosvm_git.bb \
    meta-qti-automotive/recipes-virt/safelinux-system-cfg/safelinux-system-cfg_git.bb \
    meta-qti-automotive/recipes-virt/vhost-device/vhost-device-gpio_0.1.0.bb \
    meta-qti-automotive/recipes-virt/vhost-device/vhost-device-i2c_0.1.0.bb \
    meta-qti-automotive/recipes-virt/vhost-device/vhost-device-ssr_0.1.0.bb \
    meta-qti-automotive/recipes-virt/vhost-user-lib/vhost-user-lib_git.bb \
    meta-qti-automotive/recipes-virt/vhost-user-q/vhost-user-q_git.bb \
    meta-qti-automotive/recipes-virt/vhost-user-scmi/vhost-user-scmi_git.bb \
    meta-qti-automotive/recipes-virt/vmm-boot-lcm/vmm-boot-lcm_git.bb \
    meta-qti-automotive/recipes-multimedia/virtio-video/virtio-video-header_git.bb \
"

# Missing recipes in baseline LV.AU.0.5.3 (used by qclinux-gvm-gen5)
MISSING_RECIPE_SRCLIST_BASELINE_LV053 += "\
    meta-qti-auto-kernel/recipes-kernel/linux/linux-qcom-custom-rt_6.6.bbappend \
    meta-qti-auto-kernel/recipes-kernel/linux/linux-qcom-custom_6.6.bbappend \
    meta-qti-auto-kernel/recipes-kernel/edk2/edk2_git.bbappend \
    meta-qti-auto-kernel/recipes-kernel/oot-dtbo/oot-dtbo.bbappend \
    meta-qti-automotive/recipes-core/edk2/edk2_git.bbappend \
    meta-qti-automotive/recipes-audio/acdb/acdb_git.bb \
    meta-qti-automotive/recipes-audio/ar-audio-init-service/ar-audio-init-service_git.bb \
    meta-qti-automotive/recipes-audio/ar-audio-service-utils/ar-audio-service-utils_git.bb \
    meta-qti-automotive/recipes-audio/ar-dev-plugin/ar-dev-plugin_git.bb \
    meta-qti-automotive/recipes-audio/ar-osal/ar-osal_git.bb \
    meta-qti-automotive/recipes-audio/ar-scmi/ar-scmi_git.bb \
    meta-qti-automotive/recipes-audio/ar-util/ar-util_git.bb \
    meta-qti-automotive/recipes-audio/ar2-audio-service/ar2-audio-service_git.bb \
    meta-qti-automotive/recipes-audio/ats/ats_git.bb \
    meta-qti-automotive/recipes-audio/audio-device-manager/audio-device-manager_git.bb \
    meta-qti-automotive/recipes-audio/audio-plugin-intf/audio-plugin-intf_git.bb \
    meta-qti-automotive/recipes-audio/audio-utils/audio-utils_git.bb \
    meta-qti-automotive/recipes-audio/audiolite-dlkm/audiolite-devicetree_git.bb \
    meta-qti-automotive/recipes-audio/audiolite-dlkm/audiolite-dlkm_git.bb \
    meta-qti-automotive/recipes-audio/audioreach-conf/audioreach-conf_git.bb \
    meta-qti-automotive/recipes-audio/gpr/gpr_git.bb \
    meta-qti-automotive/recipes-audio/gsl/gsl_git.bb \
    meta-qti-automotive/recipes-audio/sound-card-info-common-header/sound-card-info-common-header_git.bb \
    meta-qti-automotive/recipes-audio/sound-card-info-util/sound-card-info-util_git.bb \
    meta-qti-automotive/recipes-audio/spf/spf_git.bb \
    meta-qti-automotive/recipes-core/bootkpi-logging/bootkpi-logging.bb \
    meta-qti-automotive/recipes-core/early-service-infra/early-service-infra_git.bb \
    meta-qti-automotive/recipes-core/irqbalance/irqbalance_git.bbappend \
    meta-qti-automotive/recipes-core/notify_aop/notify-aop_git.bb \
    meta-qti-automotive/recipes-core/platform-config/platform-config_git.bb \
    meta-qti-automotive/recipes-core/power-utils/power-utils_git.bb \
    meta-qti-automotive/recipes-data/aurix-can/aurix-can_git.bb \
    meta-qti-automotive/recipes-display/display-la/display-la-headers.bb \
    meta-qti-automotive/recipes-extra/recipes-core/scmi-test/scmi-test_1.0.bb \
    meta-qti-automotive/recipes-graphics/ \
    meta-qti-automotive/recipes-kernel/edk2/edk2_git.bb \
    meta-qti-automotive/recipes-kernel/linux-msm/linux-msm-headers_6.12.bb \
    meta-qti-automotive/recipes-kernel/oot-dtbo/oot-dtbo.bb \
    meta-qti-automotive/recipes-kernel/dtc/dtc-aosp.bb \
    meta-qti-automotive/recipes-kernel/linux-msm/qcom-dtc-native.bb \
    meta-qti-automotive/recipes-kiumd/dspfirmware-mount/dspfirmware-mount_git.bb \
    meta-qti-automotive/recipes-kiumd/gvm-net-config/gvm-net-config_git.bb \
    meta-qti-automotive/recipes-kiumd/kiumd-headers/kiumd-headers_git.bb \
    meta-qti-automotive/recipes-kiumd/safelinux-cfg-modules/safelinux-cfg-modules_git.bb \
    meta-qti-automotive/recipes-qcvirtio \
    meta-qti-automotive/recipes-rideware/qcnode/qcnode_2.0.bb \
    meta-qti-automotive/recipes-safelinux-dbg-modules/safelinux-dbg-modules/safelinux-dbg-modules_git.bb \
    meta-qti-automotive/recipes-security/security-modules/safelinux-sec-modules_git.bb \
    meta-qti-automotive/recipes-virt/gunyah-drivers/gunyah-drivers_git.bb \
    meta-qti-automotive/recipes-virt/hyp-udmabuf/hyp-udmabuf-test_git.bb \
    meta-qti-automotive/recipes-virt/hyp-udmabuf/hyp-udmabuf_git.bb \
    meta-qti-automotive/recipes-virt/msmhab/msmhab_git.bb \
    meta-qti-automotive/recipes-virt/qcrosvm/qcrosvm_git.bb \
    meta-qti-automotive/recipes-virt/safelinux-system-cfg/safelinux-system-cfg_git.bb \
    meta-qti-automotive/recipes-virt/vhost-device/vhost-device-gpio_0.1.0.bb \
    meta-qti-automotive/recipes-virt/vhost-device/vhost-device-i2c_0.1.0.bb \
    meta-qti-automotive/recipes-virt/vhost-device/vhost-device-ssr_0.1.0.bb \
    meta-qti-automotive/recipes-virt/vhost-user-lib/vhost-user-lib_git.bb \
    meta-qti-automotive/recipes-virt/vhost-user-q/vhost-user-q_git.bb \
    meta-qti-automotive/recipes-virt/vhost-user-scmi/vhost-user-scmi_git.bb \
    meta-qti-automotive/recipes-virt/vmm-boot-lcm/vmm-boot-lcm_git.bb \
    meta-qti-automotive/recipes-multimedia/virtio-video/virtio-video-header_git.bb \
"
