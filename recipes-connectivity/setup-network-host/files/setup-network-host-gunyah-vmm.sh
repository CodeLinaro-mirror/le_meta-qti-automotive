#!/bin/sh
# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
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

soc_num=`gpioget 0 29`

echo " setup-network-host start "

# make sure 8021q module loaded
modprobe 8021q

# setup the bridge br0, interface eth0 and vlan br0.4
# setup the bridge br1, interface eth1 and vlan br0.10
if [ ${soc_num} -eq 0 ];
then
    setup_network $soc_num br0 192.168.1.1 eth0 4  192.168.4.1/24
    setup_network $soc_num br1 192.168.6.1 eth1 10 192.168.10.1/24
else
    setup_network $soc_num br0 192.168.1.2 eth0 4  192.168.4.2/24
    setup_network $soc_num br1 192.168.6.2 eth1 10 192.168.10.2/24
fi

echo " setup-network-host end "
