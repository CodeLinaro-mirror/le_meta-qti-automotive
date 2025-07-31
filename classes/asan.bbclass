#Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
#SPDX-License-Identifier: BSD-3-Clause-Clear

# Add AddressSanitizer function for recipes.
python __anonymous() {
    # Determine whether this function needs to be turned on
    is_turn = bb.utils.contains('DISTRO_FEATURES', 'asan', True, False, d)
    if not is_turn:
        return

    # Determine whether it is a bb file
    recipe_file = d.getVar('FILE', True)
    is_bb = recipe_file.endswith('.bb')
    if not is_bb:
        return

    # Determine whether it is under meta-qti-xxx path
    pre_pathname = d.getVar('PATH_TO_REPO')
    repo_path = pre_pathname.replace("git://", "")

    is_qti_path = recipe_file.startswith(repo_path + "/layers/meta-qti-automotive/")
    is_qti_prop_path = recipe_file.startswith(repo_path + "/layers/meta-qti-automotive-prop/")
    if not is_qti_path and not is_qti_prop_path:
        return

    # Determine whether it is a native build type, if it is, it will not inherit
    is_native = bb.utils.contains('BBCLASSEXTEND', 'native', True, False, d)
    if is_native:
        return

    recipe_name = d.getVar('PN', True)

    if "native" in recipe_name or "linux" in recipe_name or "packagegroup" in recipe_name or "kernel" in recipe_name:
        return

    if recipe_name in ['cntvct-log', 'system-core-adbd', 'wayland-ivi-extension']:
        return

    d.appendVar('DEPENDS', ' gcc-sanitizers')
    d.appendVar('LDFLAGS', ' -lasan')

    # Inherit meson, needs add compile flags at meson-configure file additionally
    if recipe_name in ['weston-sdm-extension', 'gstreamer1.0-plugins-extpoolsink', 'gstreamer1.0-plugins-qvdeinterlace', 'gstreamer1.0-plugins-vidc', 'gstreamer1.0-plugins-qcarcamsrc', 'gstreamer1.0-qvconv']:
        d.appendVar('EXTRA_OEMESON', ' -DASAN=true')
        return

    if recipe_name in ['fastcv', 'fastcv-noship']:
        d.appendVar('EXTRA_OECONF', ' --enable-asan')
        return

    d.appendVar('CFLAGS', ' -fsanitize=address')
    d.appendVar('CPPFLAGS', ' -fsanitize=address')


    # Using CMakefile.txt, needs add link library additionally
    if recipe_name in ['safetylibs', 'camera-qcx', 'safetymonitor', 'compute-resmon', 'fadas', 'sv-auto-noship', 'apss-stl']:
        d.appendVar('EXTRA_OECMAKE', ' -DASAN=ON')
}

ROOTFS_POSTPROCESS_COMMAND:append = " ${@bb.utils.contains("DISTRO_FEATURES", "asan", "add_asan_preload;", "", d)}"
#add some asan option for service
add_asan_preload() {
service_etc_list="\
 safetymonitor.service \
"
service_lib_list="\
 ab-updater.service \
 ssgtz-daemon.service \
 hyp-video-be.service \
 sv.service \
 vmm-boot-lcm.service \
 vmm-pwr-key.service \
 kgsl@.service \
 gsl_hab_server.service \
 qcx_server.service \
 vhost-device-ssr.service \
"

    for service_etc in $service_etc_list; do
       service_etc_file="${IMAGE_ROOTFS}/etc/systemd/system/${service_etc}"
       if [ -f "$service_etc_file" ]; then
           sed -i '/^\[Service\]/a Environment="LD_PRELOAD=/usr/lib/libasan.so.8"' "$service_etc_file"
       fi
    done

    for service_lib in $service_lib_list; do
       service_lib_file="${IMAGE_ROOTFS}${systemd_unitdir}/system/${service_lib}"
       if [ -f "$service_lib_file" ]; then
           sed -i '/^\[Service\]/a Environment="LD_PRELOAD=/usr/lib/libasan.so.8"' "$service_lib_file"
       fi
    done
}
