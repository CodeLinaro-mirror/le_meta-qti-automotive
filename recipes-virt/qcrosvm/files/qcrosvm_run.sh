#!/bin/sh
# Copyright (c) 2024-2025 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear


if [ ! -f /firmware/vm/boot/autoghgvm.mdt ]; then
    echo "Mount gvm bootloader ..."
    mount -o remount rw /
    mkdir -p /firmware/vm/boot
    mount /dev/disk/by-partlabel/la_bootloader_a /firmware/vm/boot
    echo "Mounted gvm bootloader on /firmware/vm/boot"
fi

echo "Launch qcrosvm ..."
/usr/bin/qcrosvm \
--vm=autoghgvm \
--use-non-protected-virtio \
--disk=/dev/disk/by-partlabel/la_devinfo,label=21,rw=true \
--disk=/dev/disk/by-partlabel/la_init_boot_a,label=22,rw=true \
--disk=/dev/disk/by-partlabel/la_init_boot_b,label=23,rw=true \
--disk=/dev/disk/by-partlabel/la_v_boot_a,label=24,rw=true \
--disk=/dev/disk/by-partlabel/la_v_boot_b,label=25,rw=true \
--disk=/dev/disk/by-partlabel/dsp_a,label=26,rw=false \
--disk=/dev/disk/by-partlabel/dsp_b,label=27,rw=false \
--disk=/dev/disk/by-partlabel/la_dtbo_a,label=28,rw=true \
--disk=/dev/disk/by-partlabel/la_dtbo_b,label=29,rw=true \
--disk=/dev/disk/by-partlabel/la_boot_a,label=2A,rw=true \
--disk=/dev/disk/by-partlabel/la_boot_b,label=2B,rw=true \
--disk=/dev/disk/by-partlabel/bluetooth_a,label=2C,rw=false \
--disk=/dev/disk/by-partlabel/bluetooth_b,label=2D,rw=false \
--disk=/dev/disk/by-partlabel/modem_a,label=2E,rw=false \
--disk=/dev/disk/by-partlabel/modem_b,label=2F,rw=false \
--disk=/dev/disk/by-partlabel/la_vbmeta_a,label=30,rw=true \
--disk=/dev/disk/by-partlabel/la_vbmeta_b,label=31,rw=true \
--disk=/dev/disk/by-partlabel/la_misc,label=32,rw=true \
--disk=/dev/disk/by-partlabel/la_persist,label=33,rw=true \
--disk=/dev/disk/by-partlabel/la_metadata,label=34,rw=true \
--disk=/dev/disk/by-partlabel/la_userdata,label=35,rw=true \
--disk=/dev/disk/by-partlabel/la_super,label=36,rw=true \
--net=true,label=38,ip_addr=10.10.10.10,netmask=255.255.255.0,mac=5A:6F:F0:05:7C:24 \
--console=stdio,label=41 \
--vsock cid=100,label=43
