# mask QCLINUX origin kernel recipes, use automotive's in qclinux1.0
BBMASK:append = " meta-qcom"
BBMASK:append = " meta-qti-auto-kernel/recipes-products/images/qti-image-ark-boot.bbappend"
BBMASK:append = " meta-qti-bsp/dynamic-layers/openembedded-layer/recipes-devtools/rsyslog/rsyslog_%.bbappend"
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

# Missing recipes globally
MISSING_RECIPE_SRCLIST += "\
    meta-qti-automotive/recipes-bsp/abctl/abctl_1.0.bb \
    meta-qti-automotive/recipes-bsp/libgpt/libgpt_1.0.bb \
    meta-qti-automotive/recipes-audio/agm-alsa-plugin/agm-alsa-plugin_git.bb \
    meta-qti-automotive/recipes-audio/agm_client/agm-client_git.bb \
    meta-qti-automotive/recipes-audio/agm_server/agm-server_git.bb \
    meta-qti-automotive/recipes-audio/agm-sndparser/agm-sndparser_git.bb \
    meta-qti-automotive/recipes-audio/agm-test/agm-test_git.bb \
    meta-qti-automotive/recipes-audio/agm-tinyalsa-plugin/agm-tinyalsa-plugin_git.bb \
    meta-qti-automotive/recipes-audio/ar-audiodlkm/ar-audiodlkm_git.bb \
    meta-qti-automotive/recipes-audio/ar-pal/ar-pal_git.bb \
    meta-qti-automotive/recipes-audio/audiodlkm/audiodlkm_git.bb \
    meta-qti-automotive/recipes-audio/pal-control-plugin/pal-control-plugin_git.bb \
    meta-qti-automotive/recipes-audio/pulseaudio-module-acdb/pulseaudio-module-acdb_git.bb \
    meta-qti-automotive/recipes-audio/pulseaudio-module-codec-control/pulseaudio-module-codec-control_git.bb \
    meta-qti-automotive/recipes-audio/pulseaudio-module-codec-control/pulseaudio-module-codec-control-headers_git.bb \
    meta-qti-automotive/recipes-bsp/libsensors/libsensors_git.bb \
    meta-qti-automotive/recipes-camera/cameradlkm/cameradlkm_0.1.bb \
    meta-qti-automotive/recipes-camera/cameradlkm/cameradlkm_0.2.bb \
    meta-qti-automotive/recipes-core/reboot-daemon/reboot-daemon_2.0.bb \
    meta-qti-automotive/recipes-data/avb-utils/avb-utils_1.0.bb \
    meta-qti-automotive/recipes-data/dataeth-dlkm/dataeth-dlkm_git.bb \
    meta-qti-automotive/recipes-data/emac-dwc-eqos/emac-dwc-eqos_git.bb \
    meta-qti-automotive/recipes-data/ptp-virtual/ptp-virtual_git.bb \
    meta-qti-automotive/recipes-graphics/graphics-driver/graphicsdlkm_git.bb \
    meta-qti-automotive/recipes-multimedia/dsp-kernel/dsp-kernel_git.bb \
    meta-qti-automotive/recipes-multimedia/media/media_0.1.bb \
    meta-qti-automotive/recipes-multimedia/videodlkm/videodlkm_git.bb \
    meta-qti-automotive/recipes-security/securemsmdlkm/securemsmdlkm_git.bb \
"

# Missing recipes in baseline LY.AU.0.1.1 (used by gen5)
MISSING_RECIPE_SRCLIST_BASELINE_LY011 += "\
    meta-qti-automotive/recipes-wlan/wlan-platform-dlkm/wlan-platform-dlkm_git.bb \
    meta-qti-automotive/recipes-wlan/wlan/qcacld32-ll-genoa_git.bb \
    meta-qti-automotive/recipes-wlan/wlan/qcacld32-ll-hasting-cnss0_git.bb \
    meta-qti-automotive/recipes-wlan/wlan/qcacld32-ll-hasting-cnss2_git.bb \
    meta-qti-automotive/recipes-wlan/wlan/qcacld32-ll-hasting_git.bb \
    meta-qti-automotive/recipes-wlan/wlan/qcacld32-ll-hmt_git.bb \
    meta-qti-automotive/recipes-wlan/wlan/qcacld32-ll-hsp_git.bb \
    meta-qti-automotive/recipes-wlan/wlan/qcacld32-ll-rome_git.bb \
    meta-qti-automotive/recipes-qcvirtio/virglrenderer/virglrenderer_1.1.0.bb \
    meta-qti-automotive-prop/recipes-qcvirtio/qcvirtio-devices/qcvirtio-devices_git.bb \
    meta-qti-automotive/recipes-kernel/qcvirtio-uclk-driver-linux/qcvirtio-uclk-driver-linux_git.bb \
    meta-qti-automotive/recipes-kernel/qcvirtio-uclk-driver-linux/qcvirtio-uclk-driver-linux-headers_git.bb \
"

# Missing recipes in baseline LY.AU.0.2.0 (used by sa8775/sa7255)
MISSING_RECIPE_SRCLIST_BASELINE_LY020 += "\
    meta-qti-automotive/recipes-audio/agm/agm_git.bb \
    meta-qti-automotive/recipes-audio/acdb/acdb_git.bb \
    meta-qti-automotive/recipes-audio/ar-osal/ar-osal_git.bb \
    meta-qti-automotive/recipes-audio/ar-util/ar-util_git.bb \
    meta-qti-automotive/recipes-audio/ats/ats_git.bb \
    meta-qti-automotive/recipes-audio/audioreach-conf/audioreach-conf_git.bb \
    meta-qti-automotive/recipes-audio/gpr/gpr_git.bb \
    meta-qti-automotive/recipes-audio/gsl/gsl_git.bb \
    meta-qti-automotive/recipes-audio/spf/spf_git.bb \
    meta-qti-automotive/recipes-display/display-intf/display-intf-headers.bb \
    meta-qti-automotive/recipes-display/display-la/display-la-headers.bb \
    meta-qti-automotive/recipes-multimedia/codec2/codec2-app_git.bb \
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
    meta-qti-automotive/recipes-multimedia/gstreamer/gstreamer1.0-plugins-vidc.bb \
    meta-qti-automotive/recipes-multimedia/gstreamer/mm-vdec-omx-test-lite_0.1.bb \
    meta-qti-automotive/recipes-multimedia/gstreamer/mm-venc-omx-test-lite_0.1.bb \
    meta-qti-automotive/recipes-multimedia/gstreamer/secure-video-app_1.0.0.bb \
    meta-qti-automotive/recipes-rideware/qcnode/qcnode_2.0.bb \
"

