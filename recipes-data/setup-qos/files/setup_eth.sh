#!/bin/sh
# Copyright (c) 2020-2021, The Linux Foundation. All rights reserved.
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

# Changes from Qualcomm Innovation Center, Inc. are provided under the following license:
# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear

DUMP_TO_KMSG=/dev/kmsg

echo "script loaded for QoS STARTED" > $DUMP_TO_KMSG
exit_status=0

check_interface_status() {
	local iface="$1"
	local max_attempts=5
	local attempt=0

	while [ $attempt -lt $max_attempts ]; do
		if ifconfig "$iface" | grep -q "UP"; then
			return 0
		else
			ifconfig "$iface" up
		fi
		sleep 1
		attempt=$((attempt+1))
	done
	echo "Failed to bring up interface $iface after $max_attempts attempts" > $DUMP_TO_KMSG
	return 1
}

print_usage() {
    echo "
Modes supported: setup_eth.sh has two pre-defined modes which can be enabled using below commands
	1) default_qos Mode (Default mode at boot up)
	2) perf_qos Mode (Needs to be enabled when running performance use cases)

Switching from one mode to another:
	eth0 interface:
		1) "/etc/initscripts/setup_eth.sh /etc/initscripts/config.ini del eth0" <- To delete the exisiting rules
		2) "/etc/initscripts/setup_eth.sh /etc/initscripts/config.ini default_qos/perf_qos eth0"  <- To install new rules

	eth1 interface:
		1) "/etc/initscripts/setup_eth.sh /etc/initscripts/config.ini del eth1" <- To delete the exisiting rules
		2) "/etc/initscripts/setup_eth.sh /etc/initscripts/config.ini default_qos/perf_qos eth1"  <- To install new rules

Usage:
	/etc/initscripts/setup_eth.sh (--help / help / h)

        /etc/initscripts/setup_eth.sh /etc/initscripts/config.ini <op> <dev>

        /etc/initscripts/setup_eth.sh /etc/initscripts/config.ini del <dev>

        /etc/initscripts/setup_eth.sh /etc/initscripts/config.ini default_qos/perf_qos <dev>

Note:
	During the 'del' operation, the VLAN interface (e.g., eth0.2) will be DELETED.
	It will be recreated automatically when you install 'perf_qos' or 'default_qos' back.
    "
}

del_tc_eth0() {
	local interface="$1"

	if [ "$eavb_vlan_id0" -ne 0 ]; then
		# Bring the VLAN interface down
		ifconfig $interface.$eavb_vlan_id0 down
		# Remove the VLAN configuration
		vconfig rem $interface.$eavb_vlan_id0
	fi
	tc qdisc delete dev $interface handle $mqprio_handle0: parent root mqprio
	exit_status=$?
	if [ $exit_status -ne 0 ]
	then
		echo "Failed to delete mqprio cbs sfq. Please check if mqprio configuration is there or not. "
		echo "Filter del did'nt have chance to be executed."
		echo "Clsact del did'nt have chance to be executed."
		return 255
	fi
	tc filter del dev $interface egress
	exit_status=$?
	if [ $exit_status -ne 0 ]
	then
		echo "Failed to delete filters."
		return 255
	fi
	tc qdisc del dev $interface clsact
	exit_status=$?
	if [ $exit_status -ne 0 ]
	then
		echo "Failed to remove clsact."
		return 255
	fi
}

del_tc_eth1() {
	local interface="$1"

	if [ "$eavb_vlan_id1" -ne 0 ];
	then
		# Bring the interface down first
		ifconfig $interface.$eavb_vlan_id1 down
		# Remove the VLAN interface
		vconfig rem $interface.$eavb_vlan_id1
	fi
	tc qdisc delete dev $interface handle $mqprio_handle1: parent root mqprio
	exit_status=$?
	if [ $exit_status -ne 0 ]
	then
		echo "Failed to delete mqprio cbs sfq. Please check if mqprio configuration is there or not. "
		echo "Filter del did'nt have chance to be executed."
		echo "Clsact del did'nt have chance to be executed."
		return 255
	fi
	tc filter del dev $interface egress
	exit_status=$?
	if [ $exit_status -ne 0 ]
	then
		echo "Failed to delete filters."
		return 255
	fi
	tc qdisc del dev $interface clsact
	exit_status=$?
	if [ $exit_status -ne 0 ]
	then
		echo "Failed to remove clsact."
		return 255
	fi
}

add_default_tc_eth0() {
	local interface="$1"

	if ! check_interface_status $interface; then
		echo "Failed to bring up interface $interface, skipping configuration" > $DUMP_TO_KMSG
		exit 0
	fi
	tc qdisc add dev $interface handle $mqprio_handle0: parent root mqprio num_tc $num_tc0 map $mqprio_map0 queues $queue_map0 hw 0
	tc qdisc add dev $interface clsact
	tc filter add dev $interface egress prio 0 u32 match u16 0x88f7 0xffff at -2 action skbedit queue_mapping 1
	tc filter add dev $interface egress prio 0 u32 match u32 0x400222f0 0xffffffff at -4 action skbedit queue_mapping 2
	tc filter add dev $interface egress prio 0 u32 match u32 0x600222f0 0xffffffff at -4 action skbedit queue_mapping 3
	tc filter add dev $interface egress protocol 802.1q flower vlan_id $vlan0_id0 vlan_prio $pcp0_value0 action skbedit priority $skb0_priority0
	tc filter add dev $interface egress protocol 802.1q flower vlan_id $vlan1_id0 vlan_prio $pcp1_value0 action skbedit priority $skb1_priority0
	tc filter add dev $interface egress protocol 802.1q flower vlan_id $vlan2_id0 vlan_prio $pcp2_value0 action skbedit priority $skb2_priority0
	if [ $q2_idle_slope0 -ne 0 ] && [ $q2_send_slope0 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $q2_cbs_handle0 parent $mqprio_handle0:3 cbs idleslope $q2_idle_slope0 sendslope $q2_send_slope0 hicredit $q2_hicredit0 locredit $q2_locredit0 offload 1
	fi
	if [ $q3_idle_slope0 -ne 0 ] && [ $q3_send_slope0 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $q3_cbs_handle0 parent $mqprio_handle0:4 cbs idleslope $q3_idle_slope0 sendslope $q3_send_slope0 hicredit $q3_hicredit0 locredit $q3_locredit0 offload 1
	fi
	if [ $q4_idle_slope0 -ne 0 ] && [ $q4_send_slope0 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $q4_cbs_handle0 parent $mqprio_handle0:5 cbs idleslope $q4_idle_slope0 sendslope $q4_send_slope0 hicredit $q4_hicredit0 locredit $q4_locredit0 offload 1
	fi
	if [ $q5_q6_idle_slope0 -ne 0 ] && [ $q5_q6_send_slope0 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $q5_q6_cbs_handle0 parent $mqprio_handle0:6 cbs idleslope $q5_q6_idle_slope0 sendslope $q5_q6_send_slope0 hicredit $q5_q6_hicredit0 locredit $q5_q6_locredit0 offload 1
	fi
	if [ $q7_q8_q9_idle_slope0 -ne 0 ] && [ $q7_q8_q9_send_slope0 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $q7_q8_q9_cbs_handle0 parent $mqprio_handle0:7 cbs idleslope $q7_q8_q9_idle_slope0 sendslope $q7_q8_q9_send_slope0 hicredit $q7_q8_q9_hicredit0 locredit $q7_q8_q9_locredit0 offload 1
	fi
	if [ "$tbs_enabled0" -eq 1 ];
	then
		tc qdisc replace dev $interface handle $q2_etf_handle0 parent $q2_cbs_handle0:3 etf clockid CLOCK_TAI delta $q2_delta0 offload skip_sock_check deadline_mode
		tc qdisc replace dev $interface handle $q3_etf_handle0 parent $q3_cbs_handle0:4 etf clockid CLOCK_TAI delta $q3_delta0 offload skip_sock_check deadline_mode
	fi
	if [ "$eavb_vlan_id0" -ne 0 ];
	then
		vconfig add $interface $eavb_vlan_id0
		ifconfig $interface.$eavb_vlan_id0 up
	fi
	if [ $l4_port0 -ne 0 ] && [ -n "$protocol0" ];
	then
		if [ $is_src0 -eq 1 ];
		then
			tc filter add dev $interface ingress protocol ip flower skip_sw ip_proto $protocol0 src_port $l4_port0 action drop
		else
			tc filter add dev $interface ingress protocol ip flower skip_sw ip_proto $protocol0 dst_port $l4_port0 action drop
		fi
	fi
	if [ -n "$l3_ip_address0" ];
	then
		if [ $is_src0 -eq 1 ];
		then
			tc filter add dev $interface ingress protocol ip flower skip_sw  src_ip $l3_ip_address0 action drop
		else
			tc filter add dev $interface ingress protocol ip flower skip_sw  dst_ip $l3_ip_address0 action drop
		fi
	fi
}

add_default_tc_eth1() {
	local interface="$1"

	if ! check_interface_status $interface; then
		echo "Failed to bring up interface $interface, skipping configuration" > $DUMP_TO_KMSG
		exit 0
	fi
	tc qdisc add dev $interface handle $mqprio_handle1: parent root mqprio num_tc $num_tc1 map $mqprio_map1 queues $queue_map1 hw 0
	tc qdisc add dev $interface clsact
	tc filter add dev $interface egress prio 0 u32 match u16 0x88f7 0xffff at -2 action skbedit queue_mapping 1
	tc filter add dev $interface egress prio 0 u32 match u32 0x400222f0 0xffffffff at -4 action skbedit queue_mapping 2
	tc filter add dev $interface egress prio 0 u32 match u32 0x600222f0 0xffffffff at -4 action skbedit queue_mapping 3
	tc filter add dev $interface egress protocol 802.1q flower vlan_id $vlan0_id1 vlan_prio $pcp0_value1 action skbedit priority $skb0_priority1
	tc filter add dev $interface egress protocol 802.1q flower vlan_id $vlan1_id1 vlan_prio $pcp1_value1 action skbedit priority $skb1_priority1
	tc filter add dev $interface egress protocol 802.1q flower vlan_id $vlan2_id1 vlan_prio $pcp2_value1 action skbedit priority $skb2_priority1
	if [ $q2_idle_slope1 -ne 0 ] && [ $q2_send_slope1 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $q2_cbs_handle1 parent $mqprio_handle1:3 cbs idleslope $q2_idle_slope1 sendslope $q2_send_slope1 hicredit $q2_hicredit1 locredit $q2_locredit1 offload 1
	fi
	if [ $q3_idle_slope1 -ne 0 ] && [ $q3_send_slope1 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $q3_cbs_handle1 parent $mqprio_handle1:4 cbs idleslope $q3_idle_slope1 sendslope $q3_send_slope1 hicredit $q3_hicredit1 locredit $q3_locredit1 offload 1
	fi
	if [ $q4_idle_slope1 -ne 0 ] && [ $q4_send_slope1 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $q4_cbs_handle1 parent $mqprio_handle1:5 cbs idleslope $q4_idle_slope1 sendslope $q4_send_slope1 hicredit $q4_hicredit1 locredit $q4_locredit1 offload 1
	fi
	if [ $q5_q6_idle_slope1 -ne 0 ] && [ $q5_q6_send_slope1 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $q5_q6_cbs_handle1 parent $mqprio_handle1:6 cbs idleslope $q5_q6_idle_slope1 sendslope $q5_q6_send_slope1 hicredit $q5_q6_hicredit1 locredit $q5_q6_locredit1 offload 1
	fi
	if [ $q7_q8_q9_idle_slope1 -ne 0 ] && [ $q7_q8_q9_send_slope1 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $q7_q8_q9_cbs_handle1 parent $mqprio_handle1:7 cbs idleslope $q7_q8_q9_idle_slope1 sendslope $q7_q8_q9_send_slope1 hicredit $q7_q8_q9_hicredit1 locredit $q7_q8_q9_locredit1 offload 1
	fi
	if [ "$tbs_enabled1" -eq 1 ];
	then
		tc qdisc replace dev $interface handle $q2_etf_handle1 parent $q2_cbs_handle1:3 etf clockid CLOCK_TAI delta $q2_delta1 offload skip_sock_check deadline_mode
		tc qdisc replace dev $interface handle $q3_etf_handle1 parent $q3_cbs_handle1:4 etf clockid CLOCK_TAI delta $q3_delta1 offload skip_sock_check deadline_mode
	fi
	if [ "$eavb_vlan_id1" -ne 0 ];
	then
		vconfig add $interface $eavb_vlan_id1
		ifconfig $interface.$eavb_vlan_id1 up
	fi
	if [ $l4_port1 -ne 0 ] && [ -n "$protocol1" ];
	then
		if [ $is_src1 -eq 1 ];
		then
			tc filter add dev $interface ingress protocol ip flower skip_sw ip_proto $protocol1 src_port $l4_port1 action drop
		else
			tc filter add dev $interface ingress protocol ip flower skip_sw ip_proto $protocol1 dst_port $l4_port1 action drop
		fi

	fi
	if [ -n "$l3_ip_address1" ];
	then
		if [ $is_src1 -eq 1 ];
		then
			tc filter add dev $interface ingress protocol ip flower skip_sw  src_ip $l3_ip_address1 action drop
		else
			tc filter add dev $interface ingress protocol ip flower skip_sw  dst_ip $l3_ip_address1 action drop
		fi

	fi
}

add_perf_tc_eth0() {
	local interface="$1"

	if ! check_interface_status $interface; then
		echo "Failed to bring up interface $interface, skipping configuration" > $DUMP_TO_KMSG
		exit 0
	fi
	#Pin eth0’s IRQ to CPU0: find eth0’s IRQ from /proc/interrupts and set its smp_affinity mask to 0x1
	echo 1 > /proc/irq/$(awk '/eth0/{split($1,a,":");print a[1]; exit}' /proc/interrupts)/smp_affinity
	tc qdisc add dev $interface handle $mqprio_handle0: parent root mqprio num_tc 7 map 0 2 1 2 3 4 5 6 6 3 4 5 1 2 3 6 queues 4@0 1@4 1@5 1@6 1@7 1@8 1@9 hw 0
	tc qdisc add dev $interface clsact
	tc filter add dev $interface egress prio 0 u32 match u16 0x88f7 0xffff at -2 action skbedit queue_mapping 4
	tc filter add dev $interface egress prio 0 u32 match u32 0x400222f0 0xffffffff at -4 action skbedit queue_mapping 5
	tc filter add dev $interface egress prio 0 u32 match u32 0x600222f0 0xffffffff at -4 action skbedit queue_mapping 6
	tc filter add dev $interface egress protocol 802.1q flower vlan_id $vlan0_id0 vlan_prio $pcp0_value0 action skbedit priority 5
	tc filter add dev $interface egress protocol 802.1q flower vlan_id $vlan1_id0 vlan_prio $pcp1_value0 action skbedit priority 6
	tc filter add dev $interface egress protocol 802.1q flower vlan_id $vlan2_id0 vlan_prio $pcp2_value0 action skbedit priority 7
	if [ $q5_q6_idle_slope0_perf -ne 0 ] && [ $q5_q6_send_slope0_perf -ne 0 ];
	then
		tc qdisc replace dev $interface handle $q5_q6_cbs_handle0 parent $mqprio_handle0:6 cbs idleslope $q5_q6_idle_slope0_perf sendslope $q5_q6_send_slope0_perf hicredit $q5_q6_hicredit0_perf locredit $q5_q6_locredit0_perf offload 1
	fi
	if [ $q7_q8_q9_idle_slope0 -ne 0 ] && [ $q7_q8_q9_send_slope0 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $q7_q8_q9_cbs_handle0 parent $mqprio_handle0:7 cbs idleslope $q7_q8_q9_idle_slope0 sendslope $q7_q8_q9_send_slope0 hicredit $q7_q8_q9_hicredit0 locredit $q7_q8_q9_locredit0 offload 1
	fi
	if [ "$tbs_enabled0" -eq 1 ];
	then
		tc qdisc replace dev $interface handle $q5_etf_handle0 parent $q5_q6_cbs_handle0:6 etf clockid CLOCK_TAI delta $q5_delta0 offload skip_sock_check deadline_mode
		tc qdisc replace dev $interface handle $q6_etf_handle0 parent $mqprio_handle0:7 etf clockid CLOCK_TAI delta $q6_delta0 offload skip_sock_check deadline_mode
	fi
	if [ "$eavb_vlan_id0" -ne 0 ];
	then
		vconfig add $interface $eavb_vlan_id0
		ifconfig $interface.$eavb_vlan_id0 up
	fi
	if [ $l4_port0 -ne 0 ] && [ -n "$protocol0" ];
	then
		if [ $is_src0 -eq 1 ];
		then
			tc filter add dev $interface ingress protocol ip flower skip_sw ip_proto $protocol0 src_port $l4_port0 action drop
		else
			tc filter add dev $interface ingress protocol ip flower skip_sw ip_proto $protocol0 dst_port $l4_port0 action drop
		fi
	fi
	if [ -n "$l3_ip_address0" ];
	then
		if [ $is_src0 -eq 1 ];
		then
			tc filter add dev $interface ingress protocol ip flower skip_sw  src_ip $l3_ip_address0 action drop
		else
			tc filter add dev $interface ingress protocol ip flower skip_sw  dst_ip $l3_ip_address0 action drop
		fi
	fi
}

add_perf_tc_eth1() {
	local interface="$1"

	if ! check_interface_status $interface; then
		echo "Failed to bring up interface $interface, skipping configuration" > $DUMP_TO_KMSG
		exit 0
	fi
	tc qdisc add dev $interface handle $mqprio_handle1: parent root mqprio num_tc 7 map 0 2 1 2 3 4 5 6 6 3 4 5 1 2 3 6 queues 4@0 1@4 1@5 1@6 1@7 1@8 1@9 hw 0
	tc qdisc add dev $interface clsact
	tc filter add dev $interface egress prio 0 u32 match u16 0x88f7 0xffff at -2 action skbedit queue_mapping 4
	tc filter add dev $interface egress prio 0 u32 match u32 0x400222f0 0xffffffff at -4 action skbedit queue_mapping 5
	tc filter add dev $interface egress prio 0 u32 match u32 0x600222f0 0xffffffff at -4 action skbedit queue_mapping 6
	tc filter add dev $interface egress protocol 802.1q flower vlan_id $vlan0_id1 vlan_prio $pcp0_value1 action skbedit priority 5
	tc filter add dev $interface egress protocol 802.1q flower vlan_id $vlan1_id1 vlan_prio $pcp1_value1 action skbedit priority 6
	tc filter add dev $interface egress protocol 802.1q flower vlan_id $vlan2_id1 vlan_prio $pcp2_value1 action skbedit priority 7
	if [ $q5_q6_idle_slope1_perf -ne 0 ] && [ $q5_q6_send_slope1_perf -ne 0 ];
	then
		tc qdisc replace dev $interface handle $q5_q6_cbs_handle1 parent $mqprio_handle1:6 cbs idleslope $q5_q6_idle_slope1_perf sendslope $q5_q6_send_slope1_perf hicredit $q5_q6_hicredit1_perf locredit $q5_q6_locredit1_perf offload 1
	fi
	if [ $q7_q8_q9_idle_slope1 -ne 0 ] && [ $q7_q8_q9_send_slope1 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $q7_q8_q9_cbs_handle1 parent $mqprio_handle1:7 cbs idleslope $q7_q8_q9_idle_slope1 sendslope $q7_q8_q9_send_slope1 hicredit $q7_q8_q9_hicredit1 locredit $q7_q8_q9_locredit1 offload 1
	fi
	if [ "$tbs_enabled1" -eq 1 ];
	then
		tc qdisc replace dev $interface handle $q5_etf_handle1 parent $q5_q6_cbs_handle1:6 etf clockid CLOCK_TAI delta $q5_delta1 offload skip_sock_check deadline_mode
		tc qdisc replace dev $interface handle $q6_etf_handle1 parent $mqprio_handle1:7 etf clockid CLOCK_TAI delta $q6_delta1 offload skip_sock_check deadline_mode
	fi
	if [ "$eavb_vlan_id1" -ne 0 ];
	then
		vconfig add $interface $eavb_vlan_id1
		ifconfig $interface.$eavb_vlan_id1 up
	fi
	if [ $l4_port1 -ne 0 ] && [ -n "$protocol1" ];
	then
		if [ $is_src1 -eq 1 ];
		then
			tc filter add dev $interface ingress protocol ip flower skip_sw ip_proto $protocol1 src_port $l4_port1 action drop
		else
			tc filter add dev $interface ingress protocol ip flower skip_sw ip_proto $protocol1 dst_port $l4_port1 action drop
		fi
	fi
	if [ -n "$l3_ip_address1" ];
	then
		if [ $is_src1 -eq 1 ];
		then
			tc filter add dev $interface ingress protocol ip flower skip_sw  src_ip $l3_ip_address1 action drop
		else
			tc filter add dev $interface ingress protocol ip flower skip_sw  dst_ip $l3_ip_address1 action drop
		fi
	fi
}

main() {
    if [ $# -lt 1 ]
    then
        print_usage
        return 255
    fi

    if [ "$1" = "help" ] || [ "$1" = "-h" ] || [ "$1" = "--help" ]
    then
        print_usage
        return 0
    fi

    file="$1"
    . "$1"
	shift

    if [ "$1" = "del" ]
    then
        shift
        if [ "$1" = "eth0" ]
        then
            del_tc_eth0 "$1"
            return_code=$?
            if [ $return_code -ne 0 ]; then
                return $return_code
            fi
        elif [ "$1" = "eth1" ]
        then
            del_tc_eth1 "$1"
            return_code=$?
            if [ $return_code -ne 0 ]; then
                return $return_code
            fi
        else
            print_usage
            return 255
        fi
    elif [ "$1" = "default_qos" ]
    then
	    shift
		if [ "$1" = "eth0" ]
		then
            add_default_tc_eth0 "$1"
	    elif [ "$1" = "eth1" ]
		then
            add_default_tc_eth1 "$1"
        else
            print_usage
            return 255
        fi
	elif [ "$1" = "perf_qos" ]
    then
	    shift
		if [ "$1" = "eth0" ]
		then
            add_perf_tc_eth0 "$1"
	    elif [ "$1" = "eth1" ]
		then
            add_perf_tc_eth1 "$1"
        else
            print_usage
            return 255
        fi
    else
        print_usage
    fi
}

main $@
return_code=$?
echo "script loaded for QoS DONE" > $DUMP_TO_KMSG
exit $return_code
