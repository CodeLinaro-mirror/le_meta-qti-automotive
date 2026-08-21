inherit ${@bb.utils.contains_any('PREFERRED_PROVIDER_virtual/kernel', 'linux-ack linux-ack-rt', 'qti-kernel-arch-clang', '', d)}

# It will remove the dynamic library related kernel moduel signing before build done and
#cause compile error if enable rm_workm hence the exclusion below.
RM_WORK_EXCLUDE += "${PN}"

do_configure[lockfiles] += "${TMPDIR}/qti-techpack.lock"
