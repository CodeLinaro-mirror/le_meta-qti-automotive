require recipes-kernel/linux-msm/linux-ack_6.12.bb

SUMMARY = "Linux ACK RT Kernel"
DESCRIPTION = "Linux ACK kernel with PREEMPT_RT enabled for Qualcomm GVM platforms"
SECTION = "RT kernel"

# Keep the pristine upstream patch as a verified source artifact. It targets
# v6.12.79 and does not apply directly to the Qualcomm ACK v6.12.81 tree, so
# apply the equivalent rebased patch maintained next to this recipe.
SRC_URI:append = " \
    https://cdn.kernel.org/pub/linux/kernel/projects/rt/6.12/older/patch-6.12.79-rt17.patch.xz;apply=no;sha256sum=a2c245a74557efd2a0f65b0df453434f169affefd89249b9b10539d110d9c69e \
    file://ack-rt/0001-PREEMPT_RT-6.12.79-rt17-rebase-ACK-6.12.81.patch \
    file://ack-rt/0002-locking-rwsem-export-rwsem-hook-declarations-for-RT.patch \
    file://ack-rt/preempt_rt.cfg \
"

# Append preempt_rt.cfg to SOC_DEFCONFIG (a space-separated list in
# linux-ack_6.12.bb) so it merges into the real kernel .config, setting
# CONFIG_PREEMPT_RT=y and CONFIG_EXPERT=y. do_shared_workdir:append() (base
# recipe) rebuilds auto.conf.soc/autoconf.h.soc for OOT modules from that
# same .config, so the RT settings flow through automatically.
SOC_DEFCONFIG:append = " ${WORKDIR}/ack-rt/preempt_rt.cfg"

do_shared_workdir[file-checksums] += "${THISDIR}/files/ack-rt/preempt_rt.cfg:True"

# generic_auto_debug.fragment enables DEBUG_MUTEXES, whose Kconfig dependency
# excludes PREEMPT_RT. olddefconfig removes it from the kernel configuration,
# but do_shared_workdir:append() (see above) re-adds it to the OOT module
# config because SOC_DEBUG_FRAGMENT is merged last and unconditionally sets
# it; strip it back out here.
do_shared_workdir:append() {
    sed -i '/^CONFIG_DEBUG_MUTEXES=/d' \
        "${STAGING_KERNEL_BUILDDIR}/include/config/auto.conf.soc"
    sed -i '/^#define CONFIG_DEBUG_MUTEXES\b/d' \
        "${STAGING_KERNEL_BUILDDIR}/include/generated/autoconf.h.soc"
}
