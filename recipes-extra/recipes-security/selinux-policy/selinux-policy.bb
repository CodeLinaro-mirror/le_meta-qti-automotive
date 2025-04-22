SUMMARY = "SELinux policy"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "bzip2-replacement-native checkpolicy-native m4-native policycoreutils-native semodule-utils-native"

PROVIDES = "virtual/refpolicy"

SRC_URI = "git://git.codelinaro.org/clo/yocto-mirrors/github/selinuxproject/refpolicy.git;protocol=https;branch=master;name=refpolicy;destsuffix=refpolicy \
"

# Specific config files for Poky
SRC_URI += "file://customizable_types  \
            file://setrans-mcs.conf  \
            file://0001-Make-unconfined_u-the-default-selinux-user.patch \
            file://0002-policy-modules-roles-sysadm-allow-sysadm-to-use-init.patch \
"

SRCREV_refpolicy = "429b26878be53e0b3537771a98e240e6e383ee73"

S = "${WORKDIR}/refpolicy"

RDEPENDS:${PN}-dev =+ "\
        python3-core \
"

RPROVIDES:${PN} = "refpolicy"

require sepolicy.inc

QCOM_STORE_ROOT = "/etc/selinux/policy-store"

FILES:${PN} += "\
    ${sysconfdir}/selinux/${POLICY_NAME}/ \
    ${datadir}/selinux/${POLICY_NAME}/*.pp \
    ${QCOM_STORE_ROOT}/${POLICY_NAME}/ \
"

FILES:${PN}-dev =+ "\
        ${datadir}/selinux/${POLICY_NAME}/include/ \
        ${sysconfdir}/selinux/sepolgen.conf \
"

DEFAULT_ENFORCING ??= "enforcing"

POLICY_TYPE ?= "mcs"
POLICY_NAME ?= "${POLICY_TYPE}"
POLICY_DISTRO ?= "debian"
POLICY_UBAC ?= "n"
POLICY_UNK_PERMS ?= "allow"
POLICY_DIRECT_INITRC ?= "y"
POLICY_SYSTEMD ?= "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'y', 'n', d)}"
POLICY_MONOLITHIC ?= "n"
POLICY_MLS_SENS ?= "0"
POLICY_MLS_CATS ?= "1024"
POLICY_MCS_CATS ?= "1024"

EXTRA_OEMAKE += "NAME=${POLICY_NAME} \
    TYPE=${POLICY_TYPE} \
    DISTRO=${POLICY_DISTRO} \
    UBAC=${POLICY_UBAC} \
    UNK_PERMS=${POLICY_UNK_PERMS} \
    DIRECT_INITRC=${POLICY_DIRECT_INITRC} \
    SYSTEMD=${POLICY_SYSTEMD} \
    MONOLITHIC=${POLICY_MONOLITHIC} \
    MLS_SENS=${POLICY_MLS_SENS} \
    MLS_CATS=${POLICY_MLS_CATS} \
    MCS_CATS=${POLICY_MCS_CATS} \
"

EXTRA_OEMAKE += "tc_usrbindir=${STAGING_BINDIR_NATIVE}"
EXTRA_OEMAKE += "OUTPUT_POLICY=`${STAGING_BINDIR_NATIVE}/checkpolicy -V | cut -d' ' -f1`"
EXTRA_OEMAKE += "CC='${BUILD_CC}' CFLAGS='${BUILD_CFLAGS}' PYTHON='${PYTHON}'"

disable_policy_modules () {
    for module in ${PURGE_POLICY_MODULES} ; do
        sed -i "s/^\(\<${module}\>\) *= *.*$/\1 = off/" ${S}/policy/modules.conf
    done
}

do_compile() {
    if [ -f "${WORKDIR}/modules.conf" ] ; then
        cp -f ${WORKDIR}/modules.conf ${S}/policy/modules.conf
    fi

    oe_runmake conf
    disable_policy_modules
    oe_runmake policy
}

prepare_policy_store () {
    oe_runmake 'DESTDIR=${D}' 'prefix=${D}${prefix}' install
    POL_PRIORITY=100
    POL_SRC=${D}${datadir}/selinux/${POLICY_NAME}
    POL_STORE=${D}${QCOM_STORE_ROOT}/${POLICY_NAME}
    POL_ACTIVE_MODS=${POL_STORE}/active/modules/${POL_PRIORITY}

    # Prepare to create policy store
    mkdir -p ${POL_STORE}
    mkdir -p ${POL_ACTIVE_MODS}

    # get hll type from suffix on base policy module
    HLL_TYPE=$(echo ${POL_SRC}/base.* | awk -F . '{if (NF>1) {print $NF}}')
    HLL_BIN=${STAGING_DIR_NATIVE}${prefix}/libexec/selinux/hll/${HLL_TYPE}

    for i in ${POL_SRC}/*.${HLL_TYPE}; do
        MOD_NAME=$(basename $i | sed "s/\.${HLL_TYPE}$//")
        MOD_DIR=${POL_ACTIVE_MODS}/${MOD_NAME}
        mkdir -p ${MOD_DIR}
        echo -n "${HLL_TYPE}" > ${MOD_DIR}/lang_ext
        if ! bzip2 -t $i >/dev/null 2>&1; then
            ${HLL_BIN} $i | bzip2 --stdout > ${MOD_DIR}/cil
            bzip2 -f $i && mv -f $i.bz2 $i
        else
            bunzip2 --stdout $i | \
                ${HLL_BIN} | \
                bzip2 --stdout > ${MOD_DIR}/cil
        fi
        cp $i ${MOD_DIR}/hll
    done
}

rebuild_policy () {
    cat <<-EOF > ${D}${sysconfdir}/selinux/semanage.conf
module-store = direct
[setfiles]
path = ${STAGING_DIR_NATIVE}${base_sbindir_native}/setfiles
args = -q -c \$@ \$<
[end]
[sefcontext_compile]
path = ${STAGING_DIR_NATIVE}${sbindir_native}/sefcontext_compile
args = \$@
[end]

policy-version = 33
store-root = "${QCOM_STORE_ROOT}"
EOF

    # Create policy store and build the policy
    semodule -p ${D} -s ${POLICY_NAME} -n -B
    rm -f ${D}${sysconfdir}/selinux/semanage.conf
    # no need to leave final dir created by semanage laying around
    rm -rf ${D}${QCOM_STORE_ROOT}/final
}

install_misc_files() {
    cat ${WORKDIR}/customizable_types >> \
        ${D}${sysconfdir}/selinux/${POLICY_NAME}/contexts/customizable_types

    # install setrans.conf for mls/mcs policy
    if [ -f ${WORKDIR}/setrans-${POLICY_TYPE}.conf ]; then
        install -m 0644 ${WORKDIR}/setrans-${POLICY_TYPE}.conf \
            ${D}${sysconfdir}/selinux/${POLICY_NAME}/setrans.conf
    fi

    # install policy headers
    oe_runmake 'DESTDIR=${D}' 'prefix=${D}${prefix}' install-headers

    echo "\
# busybox aliases
# quickly match up the busybox built-in tree to the base filesystem tree
/usr/lib/busybox/bin /usr/bin
/usr/lib/busybox/sbin /usr/sbin
/usr/lib/busybox/usr /usr
" >> ${D}/${sysconfdir}/selinux/${POLICY_NAME}/contexts/files/file_contexts.subs_dist
}

install_config() {
    echo "\
# This file controls the state of SELinux on the system.
# SELINUX= can take one of these three values:
#     enforcing - SELinux security policy is enforced.
#     permissive - SELinux prints warnings instead of enforcing.
#     disabled - No SELinux policy is loaded.
SELINUX=${DEFAULT_ENFORCING}
# SELINUXTYPE= can take one of these values:
#     minimum - Minimum Security protection.
#     standard - Standard Security protection.
#     mls - Multi Level Security protection.
#     targeted - Targeted processes are protected.
#     mcs - Multi Category Security protection.
SELINUXTYPE=${POLICY_NAME}
" > ${WORKDIR}/config
    install -d ${D}/${sysconfdir}/selinux
    install -m 0644 ${WORKDIR}/config ${D}/${sysconfdir}/selinux/
}

do_install() {
    prepare_policy_store
    rebuild_policy
    install_misc_files
    install_config
}

# Don't increment compilation because compile often fail if unused policy files are left in pw server
do_fetch[nostamp] = "1"
do_patch[nostamp] = "1"
do_compile[nostamp] = "1"
