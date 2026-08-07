#!/bin/sh

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
# Changes from Qualcomm Technologies, Inc. are provided under the following license:
#
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: BSD-3-Clause-Clear

CMDLINE_PATH=/proc/cmdline
AGL1_IFACE_ARRAY="eth0"
AGL2_IFACE_ARRAY="eth0"

get_gvm_version()
{
    local cmdline_value
    local system_name_value
    echo "Setup Network"
    cmdline_value=$(cat $CMDLINE_PATH)
    ## echo "[DBG] cmdline_value  ${cmdline_value}"
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

check_all_interfaces_up()
{
    local iface_name
    local iface_cnt
    local iface_arr

    iface_arr=$1
    ## echo "[DBG] iface_arr : $iface_arr"

    for iface_name in ${iface_arr}
    do
        iface_cnt=$(ifconfig -a | grep $iface_name | wc -l)

        if [ ${iface_cnt} -lt 1 ]
        then
            echo " WARN : $iface_name is not Ready"
            return 0;
        fi
    done

    return 1;
}

check_dns_conf()
{
    echo "DNS resolv.conf file is present. "
    for i in `seq 1 10` ; do
        if [ -e /etc/resolv.conf ];then
            echo "DNS resolv.conf file is present. "
            return 0;
        fi
        usleep 500000    ## 500ms
    done

    echo " WARN : resolv.conf file is not present, DNS not working !"
    return 1;
}

perf_net_config()
{
    echo "Enable network perf configuration"
    sysctl -w net.core.rmem_max=33554432
    sysctl -w net.core.wmem_max=33554432
    sysctl -w net.ipv4.udp_mem="786432 1048576 16777216"
    sysctl -w net.ipv4.tcp_mem="786432 1048576 16777216"
    sysctl -w net.core.rmem_default=16777216
    sysctl -w net.core.wmem_default=16777216
    sysctl -w net.core.optmem_max=25165824
    sysctl -w net.ipv4.tcp_rmem="8192 87380 16777216"
    sysctl -w net.ipv4.tcp_wmem="8192 87380 16777216"
    sysctl -w net.core.somaxconn=10000
    sysctl -w net.core.netdev_max_backlog=10000
    sysctl -w net.ipv4.ipfrag_high_thresh=50000000
    sysctl -w net.ipv6.ip6frag_high_thresh=50000000
    sysctl -w net.ipv4.ipfrag_time=2
    sysctl -w net.ipv6.ip6frag_time=2
}


setup_network_agl_vm_1()
{
    if [ -e /vendor/persist/enable_dhcp ];then
        echo "Start DHCP."
        check_dns_conf
        udhcpc -i eth0 -b
        echo "Start DHCP complete."
    else
        echo "Assign IP address"
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
    perf_net_config
}

setup_network_agl_vm_2()
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
    perf_net_config
}

get_gvm_version
gvm_version=$?
echo "GVM version is ${gvm_version}"

# try 10 times
for i in `seq 1 10` ; do
    if [ ${gvm_version} -eq 2 ]
    then
        check_all_interfaces_up "${AGL2_IFACE_ARRAY}"
        iface_is_up=$?
        echo "current interface status is ${iface_is_up}"
        if [ "${iface_is_up}" -eq 1 ]
        then
            setup_network_agl_vm_2
            break
        else
            echo " ERR : Ethernet Interfaces are not Ready !!!"
        fi
    else
        check_all_interfaces_up "${AGL1_IFACE_ARRAY}"
        iface_is_up=$?
        echo "current interface status is ${iface_is_up}"
        if [ "${iface_is_up}" -eq 1 ]
        then
            setup_network_agl_vm_1
            break
        else
            echo " ERR : Ethernet Interfaces are not Ready !!!"
        fi
    fi

    sleep 2
done

