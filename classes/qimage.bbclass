# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear

inherit core-image

IMAGE_FEATURES[validitems] += "sparse-image"

DEPENDS += "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'qti-avb', 'avbtool-native', '', d)} \
    ${@bb.utils.contains('IMAGE_FEATURES', 'sparse-image', 'libsparse-native', '', d)} \
"

# Make sparse rootfs by default
create_sparsesystem() {
    mv ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.${IMAGE_FSTYPES} ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.tmp
    img2simg ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.tmp ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.${IMAGE_FSTYPES}
    rm -f ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.tmp
}

do_image_ext4[postfuncs] += "${@bb.utils.contains('IMAGE_FEATURES', 'sparse-image', 'create_sparsesystem', '', d)}"

do_make_avb_image(){
    if ${@bb.utils.contains('DISTRO_FEATURES', 'qti-avb', 'true', 'false', d)}; then
        rootfs_partition_size=$(avbtool calc_min_partition_size \
                                    --image ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.${IMAGE_FSTYPES} \
                                    --partition_name system \
                                    --hash_algorithm sha256 \
                                    --do_not_generate_fec)

        if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'true', 'false', d)}; then
            #For lvgvm avb2.0, add hashtree for system image and generate vbmeta.img.
            avbtool add_hashtree_footer \
                --image ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.${IMAGE_FSTYPES} \
                --partition_name system \
                --partition_size ${rootfs_partition_size} \
                --hash_algorithm sha256 \
                --do_not_generate_fec
            avbtool make_vbmeta_image \
	        --include_descriptors_from_image ${DEPLOY_DIR_IMAGE}/${BOOTIMAGE_TARGET} \
	        --include_descriptors_from_image ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.${IMAGE_FSTYPES}\
	        --setup_rootfs_from_kernel ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.${IMAGE_FSTYPES} \
                --algorithm SHA256_RSA4096 \
                --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/vbgvm_private_key_4096.pem \
                --rollback_index 0 \
                --output ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}-vbmeta.img
            # Workaround, to keep two vbmeta images here with different vbmeta name.
            install -m 644 ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}-vbmeta.img ${DEPLOY_DIR_IMAGE}/${PRODUCT}-vbmeta.img
            install -m 644 ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}-vbmeta.img ${DEPLOY_DIR_IMAGE}/vbmeta.img
        else
            #For lv avb2.0, add hashtree for system image and generate vbmeta.img.
            avbtool add_hashtree_footer \
                --image ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.${IMAGE_FSTYPES} \
                --partition_size ${rootfs_partition_size} \
                --partition_name system  \
                --hash_algorithm sha256 \
                --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/testkey_rsa4096.pem \
                --rollback_index 0 \
                --do_not_generate_fec
            if [ -f ${DEPLOY_DIR_IMAGE}/${PRODUCT}-vendor_boot.img ]; then
               avbtool make_vbmeta_image \
                   --include_descriptors_from_image ${DEPLOY_DIR_IMAGE}/${BOOTIMAGE_TARGET} \
                   --include_descriptors_from_image ${DEPLOY_DIR_IMAGE}/${PRODUCT}-dtbo.img \
                   --include_descriptors_from_image ${DEPLOY_DIR_IMAGE}/${PRODUCT}-vendor_boot.img \
                   --include_descriptors_from_image ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.${IMAGE_FSTYPES} \
                   --setup_rootfs_from_kernel ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.${IMAGE_FSTYPES} \
                   --algorithm SHA256_RSA4096 \
                   --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/testkey_rsa4096.pem \
                   --rollback_index 0 \
                   --prop "com.android.build.boot.security_patch:${@time.strftime('%Y-%m-%d',time.gmtime())}" \
                   --prop "com.android.build.boot.os_version:${@time.strftime('%Y-%m-%d',time.gmtime())}" \
                   --output ${DEPLOY_DIR_IMAGE}/${VBMETAIMAGE_TARGET}
            else
               avbtool make_vbmeta_image \
                   --include_descriptors_from_image ${DEPLOY_DIR_IMAGE}/${BOOTIMAGE_TARGET} \
                   ${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', '--include_descriptors_from_image ${DEPLOY_DIR_IMAGE}/${PRODUCT}-dtbo.img', '', d)} \
                   --include_descriptors_from_image ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.${IMAGE_FSTYPES} \
                   --setup_rootfs_from_kernel ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.${IMAGE_FSTYPES} \
                   --algorithm SHA256_RSA4096 \
                   --key ${STAGING_DIR_NATIVE}${sysconfdir}/signing_tools/sigkeys/testkey_rsa4096.pem \
                   --rollback_index 0 \
                   --prop "com.android.build.boot.security_patch:${@time.strftime('%Y-%m-%d',time.gmtime())}" \
                   --prop "com.android.build.boot.os_version:${@time.strftime('%Y-%m-%d',time.gmtime())}" \
                   --output ${DEPLOY_DIR_IMAGE}/${VBMETAIMAGE_TARGET}
            fi
        fi
    fi
}

addtask do_make_avb_image after do_image_complete before do_build

# create dummy vm_bootloader image
VM_BOOTLOAD_IMG_CMD = " \
    dd if=/dev/zero of=${DEPLOY_DIR_IMAGE}/vm-bootloader.img bs=1M count=6 \
"
# compress the image to lemans
IMAGE_CMD:ext4:append:gh-gvm-lemans = "; \
  ${VM_BOOTLOAD_IMG_CMD} \
"

