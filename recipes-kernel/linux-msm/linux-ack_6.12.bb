inherit kernel qti-kernel-arch-clang

SUMMARY = "Linux ACK Kernel"
DESCRIPTION = "Linux ACK Kernel built with clang for Qualcomm GVM platforms"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

COMPATIBLE_MACHINE = "gvm-gen4-5|gvm-gen5"

DEPENDS += "elfutils-native kern-tools-native mkbootimg-native mkdtimg-native openssl-native pahole-native rsync-native signing-keys"
DEPENDS:append:toolchain-clang = " clang-cross-${TARGET_ARCH}"

SRC_URI = "\
    ${PATH_TO_REPO}/kernel/kernel-${PV}/kernel_platform/common/.git;protocol=${PROTO};name=common;destsuffix=kernel/kernel-${PV}/kernel_platform/common;usehead=1 \
    ${PATH_TO_REPO}/kernel/kernel-${PV}/kernel_platform/soc-repo/.git;protocol=${PROTO};name=socrepo;destsuffix=kernel/kernel-${PV}/kernel_platform/soc-repo;usehead=1 \
    "
SRC_URI:append = " file://ack/autogvm_extra.fragment"
SRC_URI:append = " file://ack/common_soc_modules.fragment"
SRC_URI:append = " file://ack/techpack_modules.fragment"
SRC_URI:append = " file://ack/0001-scripts-Makefile.lib-add-dts-to-dtbo-rule.patch"

SRCREV_common = "${AUTOREV}"
SRCREV_socrepo = "${AUTOREV}"
SRCREV_FORMAT = "common_socrepo"

S = "${WORKDIR}/kernel/kernel-${PV}/kernel_platform/common"
SOC_REPO_PATH = "${WORKDIR}/kernel/kernel-${PV}/kernel_platform/soc-repo"

PR = "r0"

# Pass KERNEL_CC/LD explicitly to KERNEL_CONFIG_COMMAND so do_configure
# uses clang (not the default bitbake CC=gcc). Without this, olddefconfig
# writes CONFIG_CC_VERSION_TEXT with the GCC version, causing a mismatch
# with do_compile (which uses KERNEL_CC=clang) and breaking OOT module
# builds with "syntax error near unexpected token '('" in the compiler
# version check (Makefile line 1867). Same pattern as linux-msm.inc line 20.
KERNEL_CONFIG_COMMAND = "oe_runmake_call -C ${S} CC="${KERNEL_CC}" LD="${KERNEL_LD}" O=${B} olddefconfig || oe_runmake -C ${S} O=${B} CC="${KERNEL_CC}" LD="${KERNEL_LD}" olddefconfig"

KERNEL_CC:append:aarch64 = " ${TOOLCHAIN_OPTIONS}"
KERNEL_LD:append:aarch64 = " ${TOOLCHAIN_OPTIONS}"

KERNEL_CONFIG = "generic_auto_defconfig"

SOC_DEFCONFIG = "${WORKDIR}/ack/autogvm_extra.fragment"
# debug fragment lives in soc-repo, fetched via SRC_URI
SOC_DEBUG_FRAGMENT = "${@bb.utils.contains_any('VARIANT', 'perf user', '', '${SOC_REPO_PATH}/arch/${ARCH}/configs/generic_auto_debug.fragment', d)}"

do_configure:prepend() {
    mkdir -p ${B}

    # ${B}/.config = generic_auto_defconfig + soc fragment + (debug fragment for debug builds)
    cp "${SOC_REPO_PATH}/arch/${ARCH}/configs/${KERNEL_CONFIG}" ${B}/.config \
        || bbfatal "Missing defconfig"
    cat "${SOC_DEFCONFIG}" >> ${B}/.config
    if [ -n "${SOC_DEBUG_FRAGMENT}" ]; then
        cat "${SOC_DEBUG_FRAGMENT}" >> ${B}/.config
    fi

    # CONFIG_GENDWARFKSYMS requires clang >= 19.x; meta-clang 18.1.5 not supported.
    echo "CONFIG_GENKSYMS=y" >> ${B}/.config
    echo "# CONFIG_GENDWARFKSYMS is not set" >> ${B}/.config

    # CONFIG_DEBUG_INFO_BTF requires clang >= 19.x; meta-clang 18.1.5 not supported.
    echo "# CONFIG_DEBUG_INFO_BTF is not set" >> ${B}/.config

    cp ${B}/.config ${B}/.preconfig
}

do_configure:append() {
    oe_runmake -C ${S} O=${B} savedefconfig
}

# Only do_shared_workdir uses fragment files (for auto.conf.soc/autoconf.h.soc).
# file-checksums here ensures it reruns when fragments change without triggering
# do_configure/do_compile (which would rebuild the full kernel Image).
do_shared_workdir[file-checksums] += "${THISDIR}/files/ack/autogvm_extra.fragment:True"
do_shared_workdir[file-checksums] += "${THISDIR}/files/ack/common_soc_modules.fragment:True"
do_shared_workdir[file-checksums] += "${THISDIR}/files/ack/techpack_modules.fragment:True"

do_shared_workdir:append() {
    # Copy soc-repo headers into STAGING_KERNEL_DIR so downstream OOT recipes
    # find them via LINUXINCLUDE without extra NOSTDINC_FLAGS.
    cp -ardf "${SOC_REPO_PATH}/include/"* "${STAGING_KERNEL_DIR}/include/"
    cp -ardf "${SOC_REPO_PATH}/arch/arm64/include/"* \
             "${STAGING_KERNEL_DIR}/arch/arm64/include/"
    bbnote "Copied soc-repo headers to STAGING_KERNEL_DIR"

    # Generate SOC_AUTO_CONF / SOC_AUTOCONF_H here
    install -d "${STAGING_KERNEL_BUILDDIR}/include/config"
    install -d "${STAGING_KERNEL_BUILDDIR}/include/generated"

    SOC_AUTO_CONF="${STAGING_KERNEL_BUILDDIR}/include/config/auto.conf.soc"
    SOC_AUTOCONF_H="${STAGING_KERNEL_BUILDDIR}/include/generated/autoconf.h.soc"

    # autoconf_patch_add FILE: emit CONFIG_xxx=val and #define lines to stdout.
    autoconf_patch_add() {
        local f="$1"
        [ -f "$f" ] || return
        while IFS= read -r line; do
            case "${line}" in CONFIG_*=*) ;; *) continue ;; esac
            local key="${line%%=*}"
            local val="${line#*=}"
            local auto_val=$(echo "${val}" | sed 's/^"\(.*\)"$/\1/')
            echo "${key}=${auto_val}"
            case "${val}" in
                y)   echo "#define ${key} 1" ;;
                m)   echo "#define ${key}_MODULE 1" ;;
                n)   ;;
                *)   echo "#define ${key} ${val}" ;;
            esac
        done < "$f"
    }

    # Merge KBA/.config + all soc fragments; later entries override earlier.
    # awk deduplicates by key (last occurrence wins).
    {
        cat "${STAGING_KERNEL_BUILDDIR}/.config"
        for f in "${WORKDIR}/ack"/*.fragment "${SOC_DEBUG_FRAGMENT}"; do
            [ -f "$f" ] && cat "$f"
        done
    } | awk -F= '/^CONFIG_[^=]+=/{seen[$1]=$0} END{for(k in seen) print seen[k]}' \
        > "${WORKDIR}/merged_soc.config"

    # Write directly to work-shared using > (create/truncate, no pseudo issues).
    autoconf_patch_add "${WORKDIR}/merged_soc.config" | grep "^CONFIG_" \
        > "${SOC_AUTO_CONF}"

    { printf "/* Automatically generated - do not edit */\n#define __LINUX_AUTOCONF_H\n\n"
      autoconf_patch_add "${WORKDIR}/merged_soc.config" | grep "^#define "
    } > "${SOC_AUTOCONF_H}"

    bbnote "Generated SOC_AUTO_CONF and SOC_AUTOCONF_H in do_shared_workdir"
}

do_deploy() {
    if [ -f ${D}/${KERNEL_IMAGEDEST}/-${KERNEL_VERSION} ]; then
        mv ${D}/${KERNEL_IMAGEDEST}/-${KERNEL_VERSION} \
           ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
    fi

    install -d ${DEPLOYDIR}
    install -m 0644 ${KERNEL_OUTPUT_DIR}/${KERNEL_IMAGETYPE} ${DEPLOYDIR}/${KERNEL_IMAGETYPE}
    install -m 0644 vmlinux ${DEPLOYDIR}
    install -m 0644 System.map ${DEPLOYDIR}

    install -d ${DEPLOYDIR}/kernel_modules
    for kmod in $(find . -name "*.ko") ; do
        install -m 0644 $kmod ${DEPLOYDIR}/kernel_modules
    done
}
