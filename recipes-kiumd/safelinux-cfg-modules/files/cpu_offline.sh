#!/bin/sh
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: BSD-3-Clause-Clear

# $1 = first CPU, $2 = last CPU
FIRST=$1
LAST=$2
PVM_SLICE=$3
GVM_SLICE=$4
SYSTEM_SLICE=$5

fix_cpu_range() {
    local input_range=$1

    local sys_max=$(awk -F '[-|,]' '{print $NF}' /sys/devices/system/cpu/possible)

    local start=$(echo "$input_range" | cut -d'-' -f1)
    local end=$(echo "$input_range" | cut -d'-' -f2)

    if [ "$end" -gt "$sys_max" ]; then
        end=$sys_max
    fi

    if [ "$start" -eq "$end" ]; then
        echo "$start"
    else
        echo "${start}-${end}"
    fi
}

for cpu in $(seq $FIRST $LAST); do
    cpu_path="/sys/devices/system/cpu/cpu${cpu}/online"
    [ -f "$cpu_path" ] && echo 0 > "$cpu_path"
done

REAL_PVM_SLICE=$(fix_cpu_range "$PVM_SLICE")

systemctl set-property --runtime pvm.slice AllowedCPUs=$REAL_PVM_SLICE
systemctl set-property --runtime gvm.slice AllowedCPUs=$GVM_SLICE
systemctl set-property --runtime system.slice AllowedCPUs=$SYSTEM_SLICE
