# This include forces the generation of persist image file for /persist partition

PERSIST_IMAGE_BASENAME ?= "${PRODUCT}-persist"
OUTPUT_PERSIST_FILE ?= "${DEPLOY_DIR_IMAGE}/${PERSISTIMAGE_TARGET}"
PERSIST_IMAGE_ROOTFS_SIZE ?= "6536668"
PERSIST_IMAGE_ROOTFS ?= "${TMPDIR}/rootfs/${PERSIST_IMAGE_BASENAME}"

ROOTFS_POSTPROCESS_COMMAND:prepend = " create_persist_rootfs; sync_host_fs; "

# Generating persist image in EXT4 format
IMAGE_PREPROCESS_COMMAND += "${@bb.utils.contains('DISTRO_FEATURES', 'persist-image', 'create_persist_img;', '', d)}"

# Add below scripts to populate more stuff to persist & data
# Move /var to RW partition(persist)
# Move /home to data to make it writable
IMAGE_PREPROCESS_COMMAND:prepend = "${@bb.utils.contains('IMAGE_FEATURES', 'read-only-rootfs', " populate_persist_usr; ", '', d)}"

# Root image is created now
create_persist_rootfs() {
   PERSIST_DIR="${IMAGE_ROOTFS}/persist"
   rm -rf ${PERSIST_IMAGE_ROOTFS}
   mkdir -p ${PERSIST_IMAGE_ROOTFS}
   if [ "$(ls -A ${PERSIST_DIR})" ]; then
      mv ${PERSIST_DIR}/* ${PERSIST_IMAGE_ROOTFS}
   fi
}

sync_host_fs() {
   /bin/sync
}
create_persist_img () {
   if ${@bb.utils.contains('IMAGE_FSTYPES', 'ext4', '/bin/true', '/bin/false', d)}; then
     create_persist_ext4_img
   fi
}

create_persist_file_contexts() {
    echo "/.* system_u:object_r:var_t:s0" > ${PERSIST_IMAGE_ROOTFS}/file_contexts
}

create_persist_ext4_img() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'true', 'false', d)}; then
        create_persist_file_contexts
        make_ext4fs ${PERSISTFS_CONFIG} ${MAKEEXT4_MOUNT_OPT} -s -S ${PERSIST_IMAGE_ROOTFS}/file_contexts -a var -l ${PERSIST_IMAGE_ROOTFS_SIZE} ${OUTPUT_PERSIST_FILE} ${PERSIST_IMAGE_ROOTFS} -i 16384
    else
        make_ext4fs ${PERSISTFS_CONFIG} ${MAKEEXT4_MOUNT_OPT} -s -l ${PERSIST_IMAGE_ROOTFS_SIZE} ${OUTPUT_PERSIST_FILE} ${PERSIST_IMAGE_ROOTFS} -i 16384
    fi
    chmod 644 ${OUTPUT_PERSIST_FILE}
}

populate_persist_usr() {
    PERSIST_IMAGE_ROOTFS="${TMPDIR}/rootfs/${PERSIST_IMAGE_BASENAME}"
    mv ${IMAGE_ROOTFS}/var/* ${PERSIST_IMAGE_ROOTFS}
    USR_IMAGE_ROOTFS="${TMPDIR}/rootfs/${USR_IMAGE_BASENAME}"
    [ -e ${IMAGE_ROOTFS}/home/ ] && cp -rpP ${IMAGE_ROOTFS}/home/* ${USR_IMAGE_ROOTFS}
    /bin/sync
}
