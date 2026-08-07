#Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
#SPDX-License-Identifier: BSD-3-Clause-Clear

DEPENDS += "dtc-native virtual/kernel rsync-native"
# When using linux-ack, depend on kernel-module-soc-modules so
# module.bbclass automatically adds soc-modules/Module.symvers to
# KBUILD_EXTRA_SYMBOLS for resolving cross-module symbols (e.g. habmm_*)
DEPENDS:append = " ${@bb.utils.contains('PREFERRED_PROVIDER_virtual/kernel', 'linux-ack', 'kernel-module-soc-modules', '', d)}"

TECHPACK_MODULE_OUT ?= ""
TECHPACK_HEADERS ?= ""
TECHPACK_HEADERS_OUT ?= ""
TECHPACK_MODULES ?= ""
TECHPACK_DTBS ?= ""
TECHPACK_DTBOS ?= ""
TECHPACK_MAKE_ARGS ?= ""

inherit deploy kernel-arch linux-kernel-base module ${@oe.utils.ifelse(d.getVar('PREFERRED_PROVIDER_virtual/kernel') == 'linux-ack',"qti-kernel-arch-clang", "")}  ${@oe.utils.ifelse(d.getVar('TECHPACK_MODULES') != "","qperf", "")}

KERNEL_VERSION = "${@oe.utils.read_file('${STAGING_KERNEL_BUILDDIR}/kernel-abiversion')}"

MAKE_TARGETS = "\
    M=${@os.path.relpath('${S}', '${STAGING_KERNEL_DIR}')} ${TECHPACK_MAKE_ARGS} \
    ${@oe.utils.ifelse(d.getVar('TECHPACK_MODULES') != '', 'modules', '')} \
    ${@oe.utils.ifelse(d.getVar('TECHPACK_DTBS') != '', 'dtbs', '')} \
    ${@oe.utils.ifelse(d.getVar('TECHPACK_DTBOS') != '', 'dtbs', '')} \
    "

DDK_BUILD = "${@oe.utils.ifelse(d.getVar('TECHPACK_MODULES') != '', 'true', '')}"
DTB_BUILD = "${@oe.utils.ifelse(d.getVar('TECHPACK_DTBS') != '', 'dtbs', '')}"

do_compile(){
    if ${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '6.12', 'true', 'false', d)} && \
       [ "${PREFERRED_PROVIDER_virtual/kernel}" = "linux-msm" ]; then
        # linux-msm 6.12: Bazel DDK build path
        cd ${BSPDIR}/kernel/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform &&

        BUILD_CONFIG=${KERNEL_BUILD_CONFIG} \
        KBUILD_OPTIONS="ARCH=arm64" \
        EXT_MODULES=../../../${EXT_MODULE} \
        ENABLE_DDK_BUILD=${DDK_BUILD} \
        SUBTARGET_REGEX=${MODNAME_MODULE} \
        TARGET_BOARD_PLATFORM=${KERNEL_ARCH} \
        VARIANT=${KERNEL_DEFCONFIG_VARIANT} \
        MODULE_OUT=${S} \
        KERNEL_KIT=${BSPDIR}/kernel/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform/out/msm-kernel-autogvm-${KERNEL_OUT_VARIANT}defconfig \
        OUT_DIR=${BSPDIR}/kernel/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform/out/msm-kernel-autogvm-${KERNEL_OUT_VARIANT}defconfig \
        ./build/build_module.sh ${DTB_BUILD}
    elif [ "${PREFERRED_PROVIDER_virtual/kernel}" = "linux-ack" ]; then
        # linux-ack: Yocto native OOT build path (modules / DTBs / DTBOs)
        qti_techpack_restore_autoconf

        if [ -n "${TECHPACK_MODULES}" ] && { [ -n "${TECHPACK_DTBS}" ] || [ -n "${TECHPACK_DTBOS}" ]; }; then
            bbfatal "qti-techpack: TECHPACK_MODULES and TECHPACK_DTBS/DTBOS cannot be set simultaneously"
        elif [ -n "${TECHPACK_MODULES}" ]; then
            # Module-only build: use module_do_compile (make O=KBA M=... modules)
            module_do_compile
        elif [ -n "${TECHPACK_DTBS}" ] || [ -n "${TECHPACK_DTBOS}" ]; then
            # DTB/DTBO-only build. Mirrors qcom-devicetree_6.12.bb:
            #   make -C KBA dtstree=relpath CONFIG_ARCH_QTI_VM=y dtbs
            #
            # Pass CONFIG_ARCH_QTI_VM=y so Kbuild ifeq/ifneq guards select
            # the correct dtbo-y targets without depending on auto.conf.
            DTSTREE_REL=$(realpath --relative-to="${STAGING_KERNEL_DIR}" "${S}")
            unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
            oe_runmake -C ${STAGING_KERNEL_BUILDDIR} \
                dtstree=${DTSTREE_REL} \
                CONFIG_ARCH_QTI_VM=y \
                CC="${KERNEL_CC}" LD="${KERNEL_LD}" \
                AR="${KERNEL_AR}" OBJCOPY="${KERNEL_OBJCOPY}" \
                HOSTCC="${BUILD_CC}" HOSTCFLAGS="${BUILD_CFLAGS}" \
                ${KERNEL_EXTRA_ARGS} \
                dtbs
        fi
    else
        # legacy Yocto OOT build path
        if [ -n "${TECHPACK_DTBS}" ] || [ -n "${TECHPACK_DTBOS}" ] || [ -n "${TECHPACK_MODULES}" ]; then
            module_do_compile
        fi
    fi
}

# qti_techpack_restore_autoconf: restore auto.conf / autoconf.h from .soc
# copies. cp -f sets mtime to now (newer than .config), preventing syncconfig.
# auto.conf.cmd has no .soc counterpart so it needs an explicit touch.
qti_techpack_restore_autoconf() {
    [ "${PREFERRED_PROVIDER_virtual/kernel}" = "linux-ack" ] || return 0
    KBA="${STAGING_KERNEL_BUILDDIR}"
    # cp -f sets mtime to now, newer than .config, preventing syncconfig.
    [ -f "${KBA}/include/config/auto.conf.soc" ] && \
        cp -f "${KBA}/include/config/auto.conf.soc" \
              "${KBA}/include/config/auto.conf"
    [ -f "${KBA}/include/generated/autoconf.h.soc" ] && \
        cp -f "${KBA}/include/generated/autoconf.h.soc" \
              "${KBA}/include/generated/autoconf.h"
    # auto.conf.cmd has no .soc counterpart; touch to suppress syncconfig.
    touch "${KBA}/include/config/auto.conf.cmd"
    bbnote "qti-techpack: restored auto.conf/autoconf.h and suppressed syncconfig"
}

do_compile[lockfiles] += "${TMPDIR}/qti-techpack.lock"

# All OOT builds (modules and DTBs) require:
#   virtual/kernel:do_shared_workdir  → KBA/ populated (auto.conf, scripts/, headers)
#   make-mod-scripts:do_configure     → make prepare/syncconfig completed
do_configure[depends] += "virtual/kernel:do_shared_workdir"
do_configure[depends] += "make-mod-scripts:do_configure"

do_install() {
    # install modules
    if [ -n "${TECHPACK_MODULES}" ]; then
        install -d ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}
        install -d ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra

        for mod in ${TECHPACK_MODULES}; do
            # Try direct path first, then search recursively under ${S}.
            # linux-msm 6.12 uses bare filenames (e.g. "socinfo_dt.ko")
            # while the actual .ko may be in a subdirectory (e.g. drivers/).
            if [ -f "${S}/$mod" ]; then
                install -m 0644 "${S}/$mod" \
                    ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra
            else
                found=$(find "${S}" -name "$mod" -type f 2>/dev/null | head -1)
                if [ -n "$found" ]; then
                    install -m 0644 "$found" \
                        ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra
                else
                    bbwarn "Module $mod not found under ${S}"
                fi
            fi
        done
    fi
    # install headers
    if [ -n "${TECHPACK_HEADERS}" ]; then

        for uapi_header_files in $(find ${TECHPACK_HEADERS}/* -name "*.h"); do
            uapi_dir_name=$(basename ${TECHPACK_HEADERS})
            uapi_base_dir=$(dirname $uapi_header_files)

            if [ -n "${TECHPACK_HEADERS_OUT}" ]; then
                out_uapi_dir_name=$(basename ${TECHPACK_HEADERS_OUT})

                if [ ! -d ${D}${includedir}/${TECHPACK_HEADERS_OUT} ]; then
                    install -d -p ${D}${includedir}/${TECHPACK_HEADERS_OUT}
                fi

                if [ ! -d ${D}${includedir}/${TECHPACK_HEADERS_OUT}/${uapi_base_dir#*"${out_uapi_dir_name}"/} ]; then
                    install -d -p ${D}${includedir}/${TECHPACK_HEADERS_OUT}/${uapi_base_dir#*"${out_uapi_dir_name}"/}

                fi

                out_dir=${D}${includedir}/${TECHPACK_HEADERS_OUT}/${uapi_base_dir#*"${out_uapi_dir_name}"/}

            elif [ ! -d ${D}${includedir}/linux-msm/${uapi_base_dir#*"${uapi_dir_name}"/} ]; then
                install -d -p ${D}${includedir}/linux-msm/${uapi_base_dir#*"${uapi_dir_name}"/}
                out_dir=${D}${includedir}/linux-msm/${uapi_base_dir#*"${uapi_dir_name}"/}
            fi

            process_one_header "$uapi_header_files" "${out_dir}"
        done
    fi

}

do_deploy() {
    if [ -n "${TECHPACK_DTBS}" ]; then
        install -d ${DEPLOYDIR}/build-artifacts/techpack-dtbs

        for dtb in ${TECHPACK_DTBS}; do
            if [ -f ${S}/$dtb ]; then
                install -m 0644 ${S}/$dtb ${DEPLOYDIR}/build-artifacts/techpack-dtbs/
            fi
        done
    fi

    if [ -n "${TECHPACK_DTBOS}" ]; then
        install -d ${DEPLOYDIR}/build-artifacts/techpack-dtbos

        for dtbo in ${TECHPACK_DTBOS}; do
            if [ -f ${S}/$dtbo ]; then
                install -m 0644 ${S}/$dtbo ${DEPLOYDIR}/build-artifacts/techpack-dtbos/
            fi
        done
    fi
}

addtask do_deploy after do_install

process_one_header() {
    cd ${STAGING_KERNEL_BUILDDIR}
    ${STAGING_KERNEL_DIR}/scripts/headers_install.sh $1 $2/$(basename $1)
}
