#!/bin/sh
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: BSD-3-Clause-Clear
set -eu

TARGET="${TARGET:-multi-user.target}"
OUT_DIR="${1:-/run/systemd/generator}"
SEARCH_DIRS="/etc/systemd/system /usr/lib/systemd/system"
WANTS_DIR="$OUT_DIR/$TARGET.wants"
SOC_ID_FILE="/sys/devices/soc0/soc_id"

find_unit() {
    _unit="$1"
    _template="$2"
    for d in $SEARCH_DIRS; do
        if [ -n "$_template" ] && [ -f "$d/$_template" ]; then
            echo "$d/$_template"
            return 0
        fi
        if [ -f "$d/$_unit" ]; then
            echo "$d/$_unit"
            return 0
        fi
    done
    return 1
}

SOC_ID="$(cat "$SOC_ID_FILE" 2>/dev/null || echo "")"
SOC_ID="$(echo "$SOC_ID" | sed 's/^0\+//')"
[ -n "$SOC_ID" ] || SOC_ID="unknown"

UNIT_LIST=""
case "$SOC_ID" in
    690) # 8797
        UNIT_LIST="kgsl@0.service kgsl@1.service"
        ;;
    742) # 8787
        UNIT_LIST="kgsl@0.service"
        ;;
    *)
        UNIT_LIST="kgsl@0.service kgsl@1.service"
        ;;
esac

[ -n "$UNIT_LIST" ] || exit 0

mkdir -p "$WANTS_DIR"

for unit in $UNIT_LIST; do
    case "$unit" in
        *@*.service)
            base="${unit%%@*}"
            template="${base}@.service"
            target="$(find_unit "$unit" "$template" 2>/dev/null || true)"
            ;;
        *.service)
            template=""
            target="$(find_unit "$unit" "" 2>/dev/null || true)"
            ;;
        *)
            echo "warn: skip invalid unit name: $unit" >&2
            continue
            ;;
    esac

    if [ -z "${target:-}" ]; then
        echo "warn: unit '$unit' not found in: $SEARCH_DIRS" >&2
        continue
    fi

    rm -f "$WANTS_DIR/$unit"
    ln -sfn "$target" "$WANTS_DIR/$unit"
done

exit 0
