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


# parameters:
# $1: soc num
# $2: interface name
# $3: ip address for interface
# $4: vlan id
# $5: ip address for vlan interface
setup_network()
{
  # setup interface and attach to bridge
  setup_if $2 $3
  echo " setup-network-host bring up $4 successfully on soc: $1"

  echo " setup-network-host add vlan $2.$4"
  add_vlan_to_if $2 $4 $5
}


# Fix Ethernet interface name order so that EMAC0 (0x23040000) -> eth0 and
# EMAC1 (0x23000000) -> eth1. The kernel probes by ascending address, so
# EMAC1 registers first and takes eth0 -- the reverse of the DT alias order.
# Identity is keyed off EMAC0's physical sysfs path, not the name, since the
# name is exactly what probe order gets wrong.
fix_eth_ifnames()
{
  emac0_path=/sys/devices/platform/soc@0/23040000.ethernet

  if [ -d "$emac0_path/net/eth0" ]; then
      :  # EMAC0 already eth0, nothing to do
  elif [ -d "$emac0_path/net/eth1" ]; then
      echo " setup-network-host EMAC0 registered as eth1, swapping eth0<->eth1"
      ip link set eth1 name eth_tmp
      ip link set eth0 name eth1
      ip link set eth_tmp name eth0
  else
      echo " setup-network-host EMAC0 under $emac0_path is neither eth0 nor eth1 -- names left unchanged"
  fi
}

soc_num=`gpioget 0 29`

echo " setup-network-host start "

# rename interfaces before anything configures them by name
fix_eth_ifnames

# make sure 8021q module loaded
modprobe 8021q

# setup interface eth0 and vlan br0.4
# setup interface eth1 and vlan br0.10
if [ ${soc_num} -eq 0 ];
then
    setup_network $soc_num eth0 192.168.1.1 4  192.168.4.1/24
    setup_network $soc_num eth1 192.168.6.1 10 192.168.10.1/24
else
    setup_network $soc_num eth0 192.168.1.2 4  192.168.4.2/24
    setup_network $soc_num eth1 192.168.6.2 10 192.168.10.2/24
fi


echo " setup-network-host end "
