#!/bin/sh
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: BSD-3-Clause-Clear

CMDLINE=$(cat /proc/cmdline)

get_cmdline_value() {
    echo "$CMDLINE" | tr ' ' '\n' | awk -F= -v key="$1" '$1==key && NF>1{print $2}'
}

GVMCONFIG=$(get_cmdline_value gvmconfig)

MODPROBE_DIR="/run/modprobe.d"
UNIFIED_BUILD_CONF_DIR="/etc/unified-build"

mkdir -p "$MODPROBE_DIR"

write_blacklist() {
    local conf="$1"
    local target_conf="$MODPROBE_DIR/$conf"
    local source_conf="$UNIFIED_BUILD_CONF_DIR/$conf"

    [ -f "$source_conf" ] || return 0

    : > "$target_conf"

    while IFS= read -r line || [ -n "$line" ]; do
        case "$line" in
            ""|\#*)
                continue
                ;;
        esac

        echo "$line" >> "$target_conf"
    done < "$source_conf"
}

case "$GVMCONFIG" in
    single-lv-gvm)
	write_blacklist "module-blacklist-single-lv-gvm.conf"
        ;;
esac

exit 0
