# This include forces the generation of a /usr userdata partition
# image file and propagates the /usr part of the filesystem
# over to it.

USR_IMAGE_BASENAME ?= "${PRODUCT}-usrfs"
USR_IMAGE_ROOTFS ?= "${TMPDIR}/rootfs/${USR_IMAGE_BASENAME}"
OUTPUT_FILE_EXT4 ?= "${DEPLOY_DIR_IMAGE}/${USERDATAIMAGE_TARGET}"

ROOTFS_POSTPROCESS_COMMAND:prepend = "clean_usr_rootfs; purge_rootfs; symlink_modules; sync_hostfs;"

# Generating userdata image in EXT4
IMAGE_PREPROCESS_COMMAND:append = "${@bb.utils.contains('IMAGE_FSTYPES','ext4',' create_usr_ext4_img;','', d)}"
do_image[depends] += "ext4-utils-native:do_populate_sysroot"
do_image[depends] += "e2fsprogs-native:do_populate_sysroot"

# Root image is created now
clean_usr_rootfs() {
   rm -rf ${USR_IMAGE_ROOTFS}
   mkdir -p ${USR_IMAGE_ROOTFS}
}

# Remove unwanted headers and sources copied into rootfs
purge_rootfs() {
    rm -rf ${IMAGE_ROOTFS}/usr/include
    rm -rf ${IMAGE_ROOTFS}/usr/src
    rm -rf ${IMAGE_ROOTFS}/src
}

symlink_modules() {

   DATA_DIR="${IMAGE_ROOTFS}/data"
   CONFIG_DIR="${DATA_DIR}/configs"
   LOGS_DIR="${DATA_DIR}/logs"

   if [ ! -d ${DATA_DIR} ]; then
       mkdir ${DATA_DIR}
   fi
   if [ ! -d ${CONFIG_DIR} ]; then
       mkdir ${CONFIG_DIR}
   fi
   if [ ! -d ${LOGS_DIR} ]; then
      mkdir ${LOGS_DIR}
   fi

   mv ${DATA_DIR}/* ${USR_IMAGE_ROOTFS}
}

sync_hostfs() {
   /bin/sync
}

create_userdata_file_contexts() {
    echo "/.* system_u:object_r:default_t:s0" > ${USR_IMAGE_ROOTFS}/file_contexts
}
create_usr_ext4_img() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'true', 'false', d)}; then
        create_userdata_file_contexts
        setfiles -m -r ${USR_IMAGE_ROOTFS} ${USR_IMAGE_ROOTFS}/file_contexts ${USR_IMAGE_ROOTFS}
    fi
    size_kb="0"
    size_bytes="${@d.getVar('USERDATA_SIZE_EXT4', True)}"
    if [ "$(expr $size_bytes % 4096)" == "0" ]; then
        size_kb=$(expr $size_bytes / 1024)
    else
        size_kb=$(expr $(expr $(expr 4096 - $(expr $size_bytes % 4096)) + $size_bytes) / 1024)
    fi
    dd if=/dev/zero of=${OUTPUT_FILE_EXT4}.full seek=$size_kb count=0 bs=1024
    mkfs.ext4  -F -i 4096 -b 4096 ${OUTPUT_FILE_EXT4}.full -d ${USR_IMAGE_ROOTFS}
    img2simg ${OUTPUT_FILE_EXT4}.full ${OUTPUT_FILE_EXT4}
    rm ${OUTPUT_FILE_EXT4}.full
}

