#!/bin/sh
# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear

# parameters: 
# $1: iface name
# $2: ip address
setup_if()
{
  # default timeout is 2 mins
  /lib/systemd/systemd-networkd-wait-online -i $1:off

  echo " setup-network-host $1 is available now"

  ip link set $1 up
  ifconfig $1 $2 up
}

# parameters:
# $1: iface name
# $2: vlan id
# $3: ip address for vlan interface
add_vlan_to_if()
{
  ip link add link $1 name $1.$2 type vlan id $2
  ip addr add $3 dev $1.$2
  ifconfig $1.$2 up
}

echo " setup-network-host start "

echo " setup-network-host set up eth0"
setup_if eth0 192.168.1.1

echo " setup-network-host set up eth1"
setup_if eth1 192.168.6.1

# make sure 8021q module loaded
modprobe 8021q

echo " setup-network-host add vlan eth0.4"
add_vlan_to_if eth0 4 192.168.4.1/24

echo " setup-network-host add vlan eth1.10"
add_vlan_to_if eth1 10 192.168.10.1/24

echo " setup-network-host end "

