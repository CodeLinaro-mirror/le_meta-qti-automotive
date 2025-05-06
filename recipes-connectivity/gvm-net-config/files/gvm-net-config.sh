#!/bin/sh
# Copyright (c) 2023-2025 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear

echo "gvm_net_config: wait for vmtap0 to be available"
/lib/systemd/systemd-networkd-wait-online -i vmtap0:off
echo "gvm_net_config: configure vmtap0"

echo "gvm_net_config: wait for br0 to be available"
/lib/systemd/systemd-networkd-wait-online -i br0:off
echo "gvm_net_config: configure br0"

# add vmtap0 to bridge br0
ip link set dev vmtap0 master br0
ifconfig vmtap0 0.0.0.0

echo "gvm_net_config: wait for vmtap1 to be available"
/lib/systemd/systemd-networkd-wait-online -i vmtap1:off
echo "gvm_net_config: configure vmtap1"

# set vmtap1 ip to 192.168.7.1, do not add to any bridge
ifconfig vmtap1 192.168.7.1

