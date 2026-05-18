#Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
#SPDX-License-Identifier: BSD-3-Clause-Clear
# Check that there is no scripts use bashisms.
DEPENDS += "checkbashisms-native"

export whitelist_files = "sfs_config \
                   gzexe \
                   ldd \
                   usb-devices \
                   zgrep \
                   zdiff \
                   systemd-notify \
                   install-sh \
                   xzdiff \
                   nvidia \
                   dhclient \
                   dhclient-script \
                   lxc.mount.hook \
                   updatedb \
                   fixfiles \
                   sa2 \
                   ebtables.common \
                   dhcpcd-run-hooks \
                   sandboxX.sh \
"

do_traverse_bashisms(){
    # Only check images in the whitelist
    if [ "${PN}" != "amzn-vendor-image" ] && [ "${PN}" != "machine-image" ]; then
        return
    fi

    # Check files with bashisms
    file_list=$(find ${IMAGE_ROOTFS}/ -type f \
    -not -path "${IMAGE_ROOTFS}/opt/ltp/*" \
    -not -path "${IMAGE_ROOTFS}/usr/lib/ltp/*" \
    -not -path "${IMAGE_ROOTFS}/var/lib/opkg/info/*" \
    -exec sh -c 'file "$1" | grep "POSIX shell script"' sh {} \; \
    | awk -F ":" '{print $1}')

    for f in $file_list
        do
            if ${STAGING_BINDIR_NATIVE}/perl-native/perl ${STAGING_BINDIR_NATIVE}/checkbashisms.pl $f 2>&1 | grep "bashism" | grep -v "could not"; then
                base_name=$(basename "$f")
                if ! echo "$whitelist_files" | grep -w "$base_name" > /dev/null; then
                    bberror "Found unsupported bashisms syntax in $f"
                fi
            fi
        done
}

do_rootfs[postfuncs] += "do_traverse_bashisms"
