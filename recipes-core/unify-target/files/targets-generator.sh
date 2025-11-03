#!/bin/sh
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: BSD-3-Clause-Clear

GENERATOR_DIR="$1"

CMDLINE=$(cat /proc/cmdline)

get_cmdline_value() {
    echo "$CMDLINE" | tr ' ' '\n' | awk -F= -v key="$1" '$1==key && NF>1{print $2}'
}

PRODCONFIG=$(get_cmdline_value prodconfig)
SLTFLAVOR=$(get_cmdline_value sltflavor)
OSCONFIG=$(get_cmdline_value osconfig)

WANTS_DIR="$GENERATOR_DIR/default.target.wants"
mkdir -p "$WANTS_DIR"

# swconfig to targets
case "$PRODCONFIG" in
    non-safe-ivi)
        ln -sf /usr/lib/systemd/system/nonsafe-ivi.target "$WANTS_DIR/"
        ;;
    safe-ivi)
        ln -sf /usr/lib/systemd/system/safe-ivi.target "$WANTS_DIR/"
        ;;
    adas)
        ln -sf /usr/lib/systemd/system/adas.target "$WANTS_DIR/"
        ;;
    flex)
        ln -sf /usr/lib/systemd/system/flex.target "$WANTS_DIR/"
        ;;
    "")
        ln -sf /usr/lib/systemd/system/nonsafe-ivi.target "$WANTS_DIR/"
        ;;
esac

# sltflavor to slt target
[ "$SLTFLAVOR" = "1" ] && ln -sf /usr/lib/systemd/system/slt.target "$WANTS_DIR/"

# osconfig to targets
case "$OSCONFIG" in
    PVM+GVM)
        ln -sf /usr/lib/systemd/system/single-gvm.target "$WANTS_DIR/"
        ;;
    PVM+2GVM)
        ln -sf /usr/lib/systemd/system/multi-gvm.target "$WANTS_DIR/"
        ;;
    PVMOnly)
        ;;
    "")
        ln -sf /usr/lib/systemd/system/single-gvm.target "$WANTS_DIR/"
        ;;
esac

exit 0
