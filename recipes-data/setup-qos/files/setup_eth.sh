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

file="$1"
. "$1"

interface="$2"

echo "script loaded for QoS STARTED" > $DUMP_TO_KMSG

if [ "eth0" = "$interface" ];
then
	tc qdisc add dev $interface handle $mqprio_handle0: parent root mqprio num_tc $num_tc0 map $mqprio_map0 queues $queue_map0 hw 0
	tc qdisc add dev $interface clsact
	tc filter add dev $interface egress prio 0 u32 match u16 0x88f7 0xffff at -2 action skbedit queue_mapping 1
	tc filter add dev $interface egress prio 0 u32 match u32 0x400222f0 0xffffffff at -4 action skbedit queue_mapping 2
	tc filter add dev $interface egress prio 0 u32 match u32 0x600222f0 0xffffffff at -4 action skbedit queue_mapping 3
	tc filter add dev $interface egress protocol 802.1q flower vlan_id $vlan0_id0 vlan_prio $pcp0_value0 action skbedit priority $skb0_priority0
	tc filter add dev $interface egress protocol 802.1q flower vlan_id $vlan1_id0 vlan_prio $pcp1_value0 action skbedit priority $skb1_priority0
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
	if [ $q5_idle_slope0 -ne 0 ] && [ $q5_send_slope0 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $q5_cbs_handle0 parent $mqprio_handle0:6 cbs idleslope $q5_idle_slope0 sendslope $q5_send_slope0 hicredit $q5_hicredit0 locredit $q5_locredit0 offload 1
	fi
	if [ "$tbs_enabled0" -eq 1 ];
	then
		tc qdisc replace dev $interface handle $q2_etf_handle0 parent $q2_cbs_handle0:3 etf clockid CLOCK_TAI delta 300000 offload skip_sock_check deadline_mode
		tc qdisc replace dev $interface handle $q3_etf_handle0 parent $q3_cbs_handle0:4 etf clockid CLOCK_TAI delta 300000 offload skip_sock_check deadline_mode
	fi
	if [ "$eavb_vlan_id0" -ne 0 ];
	then
		vconfig add $interface $eavb_vlan_id0
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
fi

if [ "eth1" = "$interface" ];
then
	tc qdisc add dev $interface handle $mqprio_handle1: parent root mqprio num_tc $num_tc1 map $mqprio_map1 queues $queue_map1 hw 0
	tc qdisc add dev $interface clsact
	tc filter add dev $interface egress prio 0 u32 match u16 0x88f7 0xffff at -2 action skbedit queue_mapping 1
	tc filter add dev $interface egress prio 0 u32 match u32 0x400222f0 0xffffffff at -4 action skbedit queue_mapping 2
	tc filter add dev $interface egress prio 0 u32 match u32 0x600222f0 0xffffffff at -4 action skbedit queue_mapping 3
	tc filter add dev $interface egress protocol 802.1q flower vlan_id $vlan0_id1 vlan_prio $pcp0_value1 action skbedit priority $skb0_priority1
	tc filter add dev $interface egress protocol 802.1q flower vlan_id $vlan1_id1 vlan_prio $pcp1_value1 action skbedit priority $skb1_priority1
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
	if [ $q5_idle_slope1 -ne 0 ] && [ $q5_send_slope1 -ne 0 ];
	then
		tc qdisc replace dev $interface handle $q5_cbs_handle1 parent $mqprio_handle1:6 cbs idleslope $q5_idle_slope1 sendslope $q5_send_slope1 hicredit $q5_hicredit1 locredit $q5_locredit1 offload 1
	fi
	if [ "$tbs_enabled1" -eq 1 ];
	then
		tc qdisc replace dev $interface handle $q2_etf_handle1 parent $q2_cbs_handle1:3 etf clockid CLOCK_TAI delta 300000 offload skip_sock_check deadline_mode
		tc qdisc replace dev $interface handle $q3_etf_handle1 parent $q3_cbs_handle1:4 etf clockid CLOCK_TAI delta 300000 offload skip_sock_check deadline_mode
        fi
	if [ "$eavb_vlan_id1" -ne 0 ];
        then
                vconfig add $interface $eavb_vlan_id1
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
fi

echo "script loaded for QoS DONE" > $DUMP_TO_KMSG
exit 0
