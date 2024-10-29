#!/bin/bash

# Copyright (c) 2019, The Linux Foundation. All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#     * Redistributions of source code must retain the above copyright
#       notice, this list of conditions and the following disclaimer.
#     * Redistributions in binary form must reproduce the above
#       copyright notice, this list of conditions and the following
#       disclaimer in the documentation and/or other materials provided
#       with the distribution.
#     * Neither the name of The Linux Foundation nor the names of its
#       contributors may be used to endorse or promote products derived
#       from this software without specific prior written permission.
#
# THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
# WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
# MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
# ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
# BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
# CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
# SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
# BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
# WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
# OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
# IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
# Changes from Qualcomm Innovation Center are provided under the following license:

# Copyright (c) 2022 Qualcomm Innovation Center, Inc. All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted (subject to the limitations in the
# disclaimer below) provided that the following conditions are met:
#
#    * Redistributions of source code must retain the above copyright
#      notice, this list of conditions and the following disclaimer.
#
#    * Redistributions in binary form must reproduce the above
#      copyright notice, this list of conditions and the following
#      disclaimer in the documentation and/or other materials provided
#      with the distribution.
#
#    * Neither the name of Qualcomm Innovation Center, Inc. nor the names of its
#      contributors may be used to endorse or promote products derived
#      from this software without specific prior written permission.
#
# NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE
# GRANTED BY THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT
# HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
# WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
# MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
# IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
# ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
# DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
# GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
# INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
# IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
# OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
# IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

CMDLINE_PATH=/proc/cmdline
AGL1_IFACE_ARRAY=(eth0)
AGL2_IFACE_ARRAY=(eth0)

AGL1_IFACE_ARRAY1=(eth1)
AGL1_IFACE_ARRAY2=(eth2)

function get_gvm_version()
{
    local cmdline_value
    local system_name_value
    echo "Setup Network"
    cmdline_value=$(cat $CMDLINE_PATH)
    system_name_value=${cmdline_value#*system_name=}
    system_name_value=${system_name_value%% *}
    echo "system_name_value=${system_name_value}!"
    case $system_name_value in
        lv) return 1
          ;;
        tgvm) return 2
          ;;
        *) return 0
          ;;
    esac
}



function check_all_interfaces_up()
{
    local iface_name
    local iface_cnt
    local iface_arr

    iface_arr=$1

    for iface_name in ${iface_arr[*]}
    do
        iface_cnt=$(ifconfig -a | grep $iface_name | wc -l)
        if [[ ${iface_cnt} -ne 1 ]]
        then
            echo " WARN : $iface_name is not Ready"
            return 0;
        fi
    done

    return 1;
}

function check_dns_conf()
{
    echo "DNS resolv.conf file is present. "
    for i in {1..10}
    do
        if [[ -e /etc/resolv.conf ]];then
            echo "DNS resolv.conf file is present. "
            return 0;
        fi
        usleep 500000    ## 500ms
    done

    echo " WARN : resolv.conf file is not present, DNS not working !"
    return 1;
}

function setup_network_agl_vm_1()
{
    if [[ -e /vendor/persist/enable_dhcp ]];then
        echo "Start DHCP."
        check_dns_conf
        udhcpc -i eth0 -b
        echo "Start DHCP complete."
    else
        echo "Assign Static IP Address for eth0"
        ifconfig eth0 192.168.1.2 up

        echo "Setup route"
        ip route add default dev eth0 via 192.168.1.10 table default
    fi

    echo "Create vlan"
    ip link add link eth0 name eth0.4 type vlan id 4
    ifconfig eth0.4 192.168.4.2 up

    echo "Enable forwarding"
    sysctl -w net.ipv4.conf.all.forwarding=1
    sysctl -w net.core.rmem_max=67108864
    sysctl -w net.core.wmem_max=67108864
    sysctl -w net.ipv4.tcp_rmem="4096 87380 33554432"
    sysctl -w net.ipv4.tcp_wmem="4096 65536 33554432"
    sysctl -p
}

function setup_network_agl_vm_2()
{
    echo "Assign IP address"
    ifconfig eth0 192.168.1.3 up

    echo "Setup route"
    ip route add default dev eth0 via 192.168.1.10

    echo "Create vlan"
    ip link add link eth0 name eth0.4 type vlan id 4
    ifconfig eth0.4 192.168.4.3 up

    echo "Enable forwarding"
    sysctl -w net.ipv4.conf.all.forwarding=1
    sysctl -p
}

get_gvm_version
gvm_version=$?

ietf1_configured=false
ietf2_configured=false
ietf3_configured=false

# try 10 times
for i in {1..10}
do
    if [[ ${gvm_version} -eq 2 ]]
    then
        check_all_interfaces_up "${AGL2_IFACE_ARRAY[*]}"
        iface_is_up=$?
        echo "current interface status is ${iface_is_up}"
        if [[ "${iface_is_up}" -eq 1 ]]
        then
            setup_network_agl_vm_2
            break
        else
            echo " ERR : Ethernet Interfaces are not Ready !!!"
        fi
    else

        if [ $ietf1_configured = true ] && [ $ietf2_configured = true ] && [ $ietf3_configured = true ]; then
            echo " eth0, eth1, and eth2 are all configured."
            break;
        fi

        check_all_interfaces_up "${AGL1_IFACE_ARRAY[*]}"
        iface1_is_up=$?
        echo "current interface status is ${iface1_is_up}"
        if [ $iface1_is_up -eq 1 ] && [ $ietf1_configured = false ]; then
            setup_network_agl_vm_1
            ietf1_configured=true
        else
            echo " ERR : Ethernet Interfaces(eth0) are not Ready or already configured !!!"
        fi

        check_all_interfaces_up "${AGL1_IFACE_ARRAY1[*]}"
        iface2_is_up=$?
        echo "current interface status is ${iface2_is_up}"
        if [ $iface2_is_up -eq 1 ] && [ $ietf2_configured = false ]; then
            echo "Qti-base:Assign Static IP Address for eth1"
            ifconfig eth1 192.168.6.2 up
            ietf2_configured=true
        else
            echo " ERR : Ethernet Interfaces(eth1) are not Ready or already configured !!!"
        fi

        check_all_interfaces_up "${AGL1_IFACE_ARRAY2[*]}"
        iface3_is_up=$?
        echo "current interface status is ${iface3_is_up}"
        if [ $iface3_is_up -eq 1 ] && [ $ietf3_configured = false ]; then
            echo "Qti-base:Assign Static IP Address for eth2"
            ifconfig eth2 192.168.7.2 up
            ietf3_configured=true
        else
            echo " ERR : Ethernet Interfaces(eth2) are not Ready or already configured !!!"
        fi
    fi

    sleep 2
done
