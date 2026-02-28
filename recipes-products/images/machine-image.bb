SUMMARY = "Machine image"
DESCRIPTION = "Build the full machine image depend on different parameters"
LICENSE = "BSD-3-Clause"

DEPENDS += "mkbootimg-native"

inherit core-image

require automotive-image.inc

# Set up for handling the generation of the /usr image
# partition...
require recipes-products/images/automotive-usr-image.inc

# Set up for handling the generation of the /persist image
require recipes-products/images/automotive-persist-image.inc

KERNEL_VERSION = "${@oe.utils.read_file('${STAGING_KERNEL_BUILDDIR}/kernel-abiversion')}"

add_extra_modules() {
    # add modules under /extra and remove qcaxxxx.ko and sat_module.ko)
    cd ${IMAGE_ROOTFS}/lib/modules/${KERNEL_VERSION}
    find extra -type f -name "*.ko" | grep -v qca |grep -v sat_module| sort >> modules.order

    # genarate modules.load from modules.order
    awk -F / '{print $NF > "modules.load"}' modules.order
}

ROOTFS_POSTPROCESS_COMMAND:append = " add_extra_modules;"
ROOTFS_POSTPROCESS_COMMAND:remove:gen5 = "add_extra_modules;"

# Makes image suitable for development (e.g. enable ssh for login, allows root logins and logins without passwords by ssh)
IMAGE_FEATURES:append = " ${@bb.utils.contains('VARIANT', 'debug', 'debug-tweaks ssh-server-openssh', '', d)}"
IMAGE_FEATURES:remove = "${@bb.utils.contains('DISTRO_FEATURES', 'qti-rumi', 'ssh-server-openssh read-only-rootfs', '', d)}"
IMAGE_FEATURES:append = " package-management"

INCOMPATIBLE_LICENSE = "GPL-3.0* LGPL-3.0* AGPL-3.0*"

# Add libgomp support
IMAGE_INSTALL += "libgomp"

do_rootfs[postfuncs] += "prune_busybox_unused"

prune_busybox_unused () {
    ROOT="${IMAGE_ROOTFS}"
    BB_DIRS="usr/lib/busybox/usr/bin usr/lib/busybox/usr/sbin"
    PATHS="usr/bin usr/sbin"

    for d in $BB_DIRS; do
        for f in "${ROOT}/${d}"/*; do
            [ -e "$f" ] || continue
            name=$(basename "$f")
            used="no"

            for p in $PATHS; do
                tgt="${ROOT}/${p}/${name}"
                [ -L "$tgt" ] || continue
                r=$(readlink "$tgt" || true)
                case "$r" in */busybox*|/usr/lib/busybox/*) used="yes";; esac
            done

            [ "$used" = "yes" ] || { echo "DELETE unused busybox applet: $d/$name"; rm -f "$f"; }
        done
    done
}
