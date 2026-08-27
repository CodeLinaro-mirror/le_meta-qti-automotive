#!/bin/sh
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: BSD-3-Clause-Clear

echo "gvm_net_config_lvgvm: wait for vmtap-lvgvm0 to be available"
/lib/systemd/systemd-networkd-wait-online -i vmtap-lvgvm0:off
echo "gvm_net_config_lvgvm: configure vmtap-lvgvm0"

echo "gvm_net_config_lvgvm: wait for br0 to be available"
/lib/systemd/systemd-networkd-wait-online -i br0:off
echo "gvm_net_config_lvgvm: configure br0"

# add vmtap-lvgvm0 to bridge br0
ip link set dev vmtap-lvgvm0 master br0
ifconfig vmtap-lvgvm0 0.0.0.0

echo "gvm_net_config_lvgvm: vmtap-lvgvm0 added into br0"
