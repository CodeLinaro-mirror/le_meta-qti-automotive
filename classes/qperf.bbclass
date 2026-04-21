#
# This class configure recipes which need customizations in production builds
#

QPERFDEPLOYDIR = "${WORKDIR}/deploy-qperf-${PN}"

python __anonymous() {
    # Append PRODUCT, VARIANT info to PV
    prd = d.getVar('PRODUCT', True)
    var = d.getVar('VARIANT', True)
    version = d.getVar('PV', True)
    if prd != "base":
        version += "_"+prd
    if var != "debug":
        version += "_"+var
    d.setVar('PV', version.replace("-","_"))

    # While building kernel module recipes add a task to
    # copy build artifacts into DEPLOY_DIR for ease of access
    if (bb.data.inherits_class("module", d)):
        bb.build.addtask('do_copy_kernel_module', 'do_module_signing do_deploy', 'do_install', d)
        bb.build.addtask('do_copy_kernel_module_setscene', '', '', d)
}

# Copy kernel modules into image specific deploy directory.
SSTATETASKS += "do_copy_kernel_module"
do_copy_kernel_module[dirs] = "${QPERFDEPLOYDIR}/kernel_modules/${PN}"
do_copy_kernel_module[stamp-extra-info] = "${MACHINE_ARCH}"
do_copy_kernel_module[sstate-inputdirs] = "${QPERFDEPLOYDIR}"
do_copy_kernel_module[sstate-outputdirs] = "${DEPLOY_DIR_IMAGE}"

python do_copy_kernel_module_setscene () {
    sstate_setscene(d)
}

do_copy_kernel_module() {
    cd ${S}
    for mod in *.ko; do
        if [ -f $mod ]; then
            install -m 0644 $mod ${QPERFDEPLOYDIR}/kernel_modules/${PN}
        fi
    done
}
