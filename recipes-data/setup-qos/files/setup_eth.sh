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

# Changes from Qualcomm Technologies, Inc. are provided under the following license:
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
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
Usage:
	/etc/initscripts/setup_eth.sh (--help / help / h)

        /etc/initscripts/setup_eth.sh /etc/initscripts/config.ini <op> <dev>

        /etc/initscripts/setup_eth.sh /etc/initscripts/config.ini del <dev>

        /etc/initscripts/setup_eth.sh /etc/initscripts/config.ini add <dev>

          EMAC VDMA/PDMA to Traffic Class (TC) Mapping Configuration:
          Transmit:
          - VDMA0,1,2,3 <-> TC0 <-> PDMA0 (Best Effort)
          - VDMA4 <-> TC1 <-> PDMA1 (PTP)
          - VDMA5 <-> TC2 <-> PDMA2 (Class B)
          - VDMA6 <-> TC3 <-> PDMA3 (Class A)
          - VDMA7 <-> TC4 <-> PDMA4 (VLAN PCP)
          - VDMA8 <-> TC5 <-> PDMA5 (VLAN PCP)
          - VDMA9 <-> TC6 <-> PDMA6,7,8,9 (VLAN PCP)
          - VDMA10 <-> TC7 <-> PDMA10 (ADSP)
          - VDMA11 <-> TC7 <-> PDMA11 (ADSP)
          Receive:
          - PDMA0 <-> TC0 <-> VDMA0 (Best Effort)
          - PDMA1 <-> TC1 <-> VDMA1 (PTP)
          - PDMA2 <-> TC2 <-> VDMA2 (AVCPQ)
          - PDMA3 <-> TC3 <-> VDMA3 (Class A/B)
          - PDMA4 <-> TC4 <-> VDMA4
          - PDMA5 <-> TC5 <-> VDMA5
          - PDMA6,7,8,9 <-> TC6 <-> VDMA6,7,8,9
          - PDMA10 <-> TC7 <-> VDMA10 (ADSP)
          - PDMA11 <-> TC7 <-> VDMA11 (ADSP)

Note:
	During the 'del' operation, the VLAN interface (e.g., eth0.2) will be DELETED.
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

add_tc_eth0() {
	local interface="$1"

	if ! check_interface_status $interface; then
		echo "Failed to bring up interface $interface, skipping configuration" > $DUMP_TO_KMSG
		exit 0
	fi
	# Configure TX interrupt coalescing on eth0 to generate an interrupt
	# after up to 128 packets are transmitted, reducing interrupt rate/CPU load
	ethtool -C $interface tx-frames 128 > /dev/null 2>&1
	# Enable Receive Packet Steering on eth0 RX queue 0 and map it to CPUs 0–5
	echo 3f000 > /sys/class/net/$interface/queues/rx-0/rps_cpus
	tc qdisc add dev $interface handle $mqprio_handle0: parent root mqprio num_tc $num_tc0 map $mqprio_map0 queues $queue_map0 hw 0
	tc qdisc add dev $interface clsact
	tc filter add dev $interface egress prio 0 u32 match u16 0x88f7 0xffff at -2 action skbedit queue_mapping 4
	tc filter add dev $interface egress prio 0 u32 match u32 0x400222f0 0xffffffff at -4 action skbedit queue_mapping 5
	tc filter add dev $interface egress prio 0 u32 match u32 0x600222f0 0xffffffff at -4 action skbedit queue_mapping 6
	tc filter add dev $interface egress protocol 802.1q flower vlan_id $vlan0_id0 vlan_prio $pcp0_value0 action skbedit priority $skb0_priority0
	tc filter add dev $interface egress protocol 802.1q flower vlan_id $vlan1_id0 vlan_prio $pcp1_value0 action skbedit priority $skb1_priority0
	tc filter add dev $interface egress protocol 802.1q flower vlan_id $vlan2_id0 vlan_prio $pcp2_value0 action skbedit priority $skb2_priority0
	if [ $vdma5_q2_idle_slope0 -ne 0 ] && [ $vdma5_q2_send_slope0 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $vdma5_q2_cbs_handle0 parent $mqprio_handle0:6 cbs idleslope $vdma5_q2_idle_slope0 sendslope $vdma5_q2_send_slope0 hicredit $vdma5_q2_hicredit0 locredit $vdma5_q2_locredit0 offload 1
	fi
	if [ $vdma6_q3_idle_slope0 -ne 0 ] && [ $vdma6_q3_send_slope0 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $vdma6_q3_cbs_handle0 parent $mqprio_handle0:7 cbs idleslope $vdma6_q3_idle_slope0 sendslope $vdma6_q3_send_slope0 hicredit $vdma6_q3_hicredit0 locredit $vdma6_q3_locredit0 offload 1
	fi
	if [ $vdma7_q4_idle_slope0 -ne 0 ] && [ $vdma7_q4_send_slope0 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $vdma7_q4_cbs_handle0 parent $mqprio_handle0:8 cbs idleslope $vdma7_q4_idle_slope0 sendslope $vdma7_q4_send_slope0 hicredit $vdma7_q4_hicredit0 locredit $vdma7_q4_locredit0 offload 1
	fi
	if [ $vdma8_q5_idle_slope0 -ne 0 ] && [ $vdma8_q5_send_slope0 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $vdma8_q5_cbs_handle0 parent $mqprio_handle0:9 cbs idleslope $vdma8_q5_idle_slope0 sendslope $vdma8_q5_send_slope0 hicredit $vdma8_q5_hicredit0 locredit $vdma8_q5_locredit0 offload 1
	fi
	if [ $vdma9_q6_q7_q8_q9_idle_slope0 -ne 0 ] && [ $vdma9_q6_q7_q8_q9_send_slope0 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $vdma9_q6_q7_q8_q9_cbs_handle0 parent $mqprio_handle0:a cbs idleslope $vdma9_q6_q7_q8_q9_idle_slope0 sendslope $vdma9_q6_q7_q8_q9_send_slope0 hicredit $vdma9_q6_q7_q8_q9_hicredit0 locredit $vdma9_q6_q7_q8_q9_locredit0 offload 1
	fi
	if [ "$tbs_enabled0" -eq 1 ];
	then
		tc qdisc replace dev $interface handle $vdma5_q2_etf_handle0 parent $vdma5_q2_cbs_handle0:6 etf clockid CLOCK_TAI delta $vdma5_q2_delta0 offload skip_sock_check deadline_mode
		tc qdisc replace dev $interface handle $vdma6_q3_etf_handle0 parent $vdma6_q3_cbs_handle0:7 etf clockid CLOCK_TAI delta $vdma6_q3_delta0 offload skip_sock_check deadline_mode
	fi
	if [ "$eavb_vlan_id0" -ne 0 ];
	then
		vconfig add $interface $eavb_vlan_id0
		ifconfig $interface.$eavb_vlan_id0 up
	fi
	if [ $l4_port0 -ne 0 ] && [ -n "$protocol0" ];
	then
		if [ "$l4_action0" = "drop" ] || [ "$l4_action0" = "pass" ]
		then
			if [ $is_src0 -eq 1 ];
			then
				tc filter add dev $interface ingress protocol ip flower skip_sw ip_proto $protocol0 src_port $l4_port0 action $l4_action0
			else
				tc filter add dev $interface ingress protocol ip flower skip_sw ip_proto $protocol0 dst_port $l4_port0 action $l4_action0
			fi
		fi
	fi
	if [ -n "$l3_ip_address0" ];
	then
		if [ "$l3_action0" = "drop" ] || [ "$l3_action0" = "pass" ]
		then
			if [ $is_src0 -eq 1 ];
			then
				tc filter add dev $interface ingress protocol ip flower skip_sw  src_ip $l3_ip_address0 action $l3_action0
			else
				tc filter add dev $interface ingress protocol ip flower skip_sw  dst_ip $l3_ip_address0 action $l3_action0
			fi
		fi
	fi
}

add_tc_eth1() {
	local interface="$1"

	if ! check_interface_status $interface; then
		echo "Failed to bring up interface $interface, skipping configuration" > $DUMP_TO_KMSG
		exit 0
	fi
	tc qdisc add dev $interface handle $mqprio_handle1: parent root mqprio num_tc $num_tc1 map $mqprio_map1 queues $queue_map1 hw 0
	tc qdisc add dev $interface clsact
	tc filter add dev $interface egress prio 0 u32 match u16 0x88f7 0xffff at -2 action skbedit queue_mapping 4
	tc filter add dev $interface egress prio 0 u32 match u32 0x400222f0 0xffffffff at -4 action skbedit queue_mapping 5
	tc filter add dev $interface egress prio 0 u32 match u32 0x600222f0 0xffffffff at -4 action skbedit queue_mapping 6
	tc filter add dev $interface egress protocol 802.1q flower vlan_id $vlan0_id1 vlan_prio $pcp0_value1 action skbedit priority $skb0_priority1
	tc filter add dev $interface egress protocol 802.1q flower vlan_id $vlan1_id1 vlan_prio $pcp1_value1 action skbedit priority $skb1_priority1
	tc filter add dev $interface egress protocol 802.1q flower vlan_id $vlan2_id1 vlan_prio $pcp2_value1 action skbedit priority $skb2_priority1
	if [ $vdma5_q2_idle_slope1 -ne 0 ] && [ $vdma5_q2_send_slope1 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $vdma5_q2_cbs_handle1 parent $mqprio_handle1:6 cbs idleslope $vdma5_q2_idle_slope1 sendslope $vdma5_q2_send_slope1 hicredit $vdma5_q2_hicredit1 locredit $vdma5_q2_locredit1 offload 1
	fi
	if [ $vdma6_q3_idle_slope1 -ne 0 ] && [ $vdma6_q3_send_slope1 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $vdma6_q3_cbs_handle1 parent $mqprio_handle1:7 cbs idleslope $vdma6_q3_idle_slope1 sendslope $vdma6_q3_send_slope1 hicredit $vdma6_q3_hicredit1 locredit $vdma6_q3_locredit1 offload 1
	fi
	if [ $vdma7_q4_idle_slope1 -ne 0 ] && [ $vdma7_q4_send_slope1 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $vdma7_q4_cbs_handle1 parent $mqprio_handle1:8 cbs idleslope $vdma7_q4_idle_slope1 sendslope $vdma7_q4_send_slope1 hicredit $vdma7_q4_hicredit1 locredit $vdma7_q4_locredit1 offload 1
	fi
	if [ $vdma8_q5_idle_slope1 -ne 0 ] && [ $vdma8_q5_send_slope1 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $vdma8_q5_cbs_handle1 parent $mqprio_handle1:9 cbs idleslope $vdma8_q5_idle_slope1 sendslope $vdma8_q5_send_slope1 hicredit $vdma8_q5_hicredit1 locredit $vdma8_q5_locredit1 offload 1
	fi
	if [ $vdma9_q6_q7_q8_q9_idle_slope1 -ne 0 ] && [ $vdma9_q6_q7_q8_q9_send_slope1 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $vdma9_q6_q7_q8_q9_cbs_handle1 parent $mqprio_handle1:a cbs idleslope $vdma9_q6_q7_q8_q9_idle_slope1 sendslope $vdma9_q6_q7_q8_q9_send_slope1 hicredit $vdma9_q6_q7_q8_q9_hicredit1 locredit $vdma9_q6_q7_q8_q9_locredit1 offload 1
	fi
	if [ "$tbs_enabled1" -eq 1 ];
	then
		tc qdisc replace dev $interface handle $vdma5_q2_etf_handle1 parent $vdma5_q2_cbs_handle1:6 etf clockid CLOCK_TAI delta $vdma5_q2_delta1 offload skip_sock_check deadline_mode
		tc qdisc replace dev $interface handle $vdma6_q3_etf_handle1 parent $vdma6_q3_cbs_handle1:7 etf clockid CLOCK_TAI delta $vdma6_q3_delta1 offload skip_sock_check deadline_mode
	fi
	if [ "$eavb_vlan_id1" -ne 0 ];
	then
		vconfig add $interface $eavb_vlan_id1
		ifconfig $interface.$eavb_vlan_id1 up
	fi
	if [ $l4_port1 -ne 0 ] && [ -n "$protocol1" ];
	then
		if [ "$l4_action1" = "drop" ] || [ "$l4_action1" = "pass" ]
		then
			if [ $is_src1 -eq 1 ];
			then
				tc filter add dev $interface ingress protocol ip flower skip_sw ip_proto $protocol1 src_port $l4_port1 action $l4_action1
			else
				tc filter add dev $interface ingress protocol ip flower skip_sw ip_proto $protocol1 dst_port $l4_port1 action $l4_action1
			fi
		fi
	fi
	if [ -n "$l3_ip_address1" ];
	then
		if [ "$l3_action1" = "drop" ] || [ "$l3_action1" = "pass" ]
		then
			if [ $is_src1 -eq 1 ];
			then
				tc filter add dev $interface ingress protocol ip flower skip_sw  src_ip $l3_ip_address1 action $l3_action1
			else
				tc filter add dev $interface ingress protocol ip flower skip_sw  dst_ip $l3_ip_address1 action $l3_action1
			fi
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
    elif [ "$1" = "add" ]
    then
	    shift
		if [ "$1" = "eth0" ]
		then
            add_tc_eth0 "$1"
	    elif [ "$1" = "eth1" ]
		then
            add_tc_eth1 "$1"
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
