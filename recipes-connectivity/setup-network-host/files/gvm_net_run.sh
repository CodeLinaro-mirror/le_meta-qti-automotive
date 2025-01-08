#!/bin/sh
# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear

echo "start gvm network setup"

# create bridge br0 and assign 192.168.1.1 to it
ip link add name br0 type bridge
ip link set br0 up
ip addr add 192.168.1.1/24 dev br0

# bring up eth0 and add to br0
ifconfig eth0 up
ip link set dev eth0 master br0

# add vmtap0 to br0
ip link set dev vmtap0 master br0
ifconfig vmtap0 0.0.0.0

# add vlan to br0
ip link add link br0 name br0.4 type vlan id 4
ip addr add 192.168.4.1/24 dev br0.4
ifconfig br0.4 up

echo "finish gvm network setup"

