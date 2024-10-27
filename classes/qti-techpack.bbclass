#Copyright (c) 2022 Qualcomm Innovation Center, Inc. All rights reserved.
#SPDX-License-Identifier: BSD-3-Clause-Clear

DEPENDS += "dtc-native virtual/kernel"

TECHPACK_MODULE_OUT ?= ""
TECHPACK_HEADERS ?= ""
TECHPACK_HEADERS_OUT ?= ""
TECHPACK_MODULES ?= ""
TECHPACK_DTBS ?= ""
TECHPACK_DTBOS ?= ""
TECHPACK_MAKE_ARGS ?= ""

inherit deploy kernel-arch linux-kernel-base module ${@oe.utils.ifelse(d.getVar('PREFERRED_PROVIDER_virtual/kernel') == 'linux-msm',"qti-kernel-arch-clang", "")} ${@oe.utils.ifelse(d.getVar('TECHPACK_MODULES') != "","qperf", "")}

KERNEL_VERSION = "${@oe.utils.read_file('${STAGING_KERNEL_BUILDDIR}/kernel-abiversion')}"

MAKE_TARGETS = "\
    M=${@os.path.relpath('${S}', '${STAGING_KERNEL_DIR}')} ${TECHPACK_MAKE_ARGS} \
    ${@oe.utils.ifelse(d.getVar('TECHPACK_MODULES') != '', 'modules', '')} \
    ${@oe.utils.ifelse(d.getVar('TECHPACK_DTBS') != '', 'dtbs', '')} \
    ${@oe.utils.ifelse(d.getVar('TECHPACK_DTBOS') != '', 'dtbs', '')} \
    "

do_compile() {
    if [ -n "${TECHPACK_DTBS}" ] || [ -n "${TECHPACK_DTBOS}" ] || [ -n "${TECHPACK_MODULES}" ]; then
        module_do_compile
    fi
}

# lock to avoid parallel compiling
do_compile[lockfiles] += "${TMPDIR}/qti-techpack.lock"
do_compile[depends] += "virtual/kernel:do_shared_workdir"

do_install() {
    # install modules
    if [ -n "${TECHPACK_MODULES}" ]; then
        install -d ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}
        install -d ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra

        for mod in ${TECHPACK_MODULES}; do
            if [ -f ${S}/$mod ]; then
                install -m 0644 ${S}/$mod ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra
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
