do_deploy_auto[dirs] = "${DEPLOY_DIR_IMAGE}"
do_deploy_auto[deptask] = "do_image_complete"
do_deploy_auto[nostamp] = "1"

# The original path where generate the boot/abl image
DEPLOY_ABLIMAGE_DIR_BASE = "${PRODUCT}-automotive"
DEPLOY_ABLIMAGE_DIR = "${DEPLOY_ABLIMAGE_DIR_BASE}${@['-' + d.getVar('VARIANT', True), ''][d.getVar('VARIANT', True) == ('' or 'debug')]}"
DEPLOY_ABLIMAGE_PATH = "${DEPLOY_DIR}/images/${DEPLOY_ABLIMAGE_DIR}"

do_deploy_auto () {
    if [ "${PN}" = "machine-image-lagvm" ] || [ "${PN}" = "machine-image-pvm" ]; then
        # copy abl image
        if [ -f ${DEPLOY_ABLIMAGE_PATH}/${PRODUCT}-abl.elf ]; then
            cp ${DEPLOY_ABLIMAGE_PATH}/${PRODUCT}-abl.elf .
        fi
        # copy vmlinux image
        if [ -f ${DEPLOY_ABLIMAGE_PATH}/vmlinux ]; then
            cp ${DEPLOY_ABLIMAGE_PATH}/vmlinux .
        fi
    fi
}

addtask do_deploy_auto after do_make_avb_image before do_build
