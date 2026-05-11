#!/bin/sh
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: BSD-3-Clause-Clear

# parameters: 
# $1: bridge name
# $2: ip address
setup_bridge()
{
  ip link add name $1 type bridge
  ip link set dev $1 up
  ifconfig $1 $2 up
}

# parameters: 
# $1: iface name
# $2: bridge name
setup_if()
{
  # default timeout is 2 mins
  /lib/systemd/systemd-networkd-wait-online -i $1:off

  echo " setup-network-host $1 is available now"

  ip link set $1 up
  ip link set $1 master $2
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

# parameters:
# $1: soc num
# $2: bridge name
# $3: ip address for bridge
# $4: interface name
# $5: vlan id
# $6: ip address for vlan interface
setup_network()
{
  setup_bridge $2 $3
  echo " setup-network-host bring up $2 successfully on soc: $1"
  
  # setup interface and attach to bridge
  setup_if $4 $2
  echo " setup-network-host bring up $4 successfully on soc: $1"
  echo " setup-network-host add $4 into $2 successfully on soc: $1"
  
  echo " setup-network-host add vlan $2.$5"
  add_vlan_to_if $2 $5 $6
}

echo " setup-network-host start "

# make sure 8021q module loaded
modprobe 8021q

echo " modprobe complete "

# setup the bridge br0, interface eth0 and vlan br0.4
setup_network 0 br0 192.168.1.1 eth0 4  192.168.4.1/24

echo " setup-network-host end "
