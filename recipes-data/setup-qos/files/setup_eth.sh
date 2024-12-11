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
	tc qdisc add dev eth0 handle $mqprio_handle0: parent root mqprio num_tc $num_tc0 map $mqprio_map0 queues 1@0 1@1 1@2 1@3 hw 0
	tc qdisc add dev eth0 clsact
	tc filter add dev eth0 egress prio 0 u32 match u16 0x88f7 0xffff at -2 action skbedit queue_mapping 1
	tc filter add dev eth0 egress prio 0 u32 match u16 0x22f0 0xffff at -2 action skbedit queue_mapping 2
	if [ $q2_idle_slope0 -ne 0 ] && [ $q2_send_slope0 -ne 0 ];
	then
		tc qdisc replace dev eth0 handle $q2_cbs_handle0 parent $mqprio_handle0:3 cbs idleslope $q2_idle_slope0 sendslope $q2_send_slope0 hicredit $q2_hicredit0 locredit $q2_locredit0 offload 1
	fi
	if [ $q3_idle_slope0 -ne 0 ] && [ $q3_send_slope0 -ne 0 ];
	then
		tc qdisc replace dev eth0 handle $q3_cbs_handle0 parent $mqprio_handle0:4 cbs idleslope $q3_idle_slope0 sendslope $q3_send_slope0 hicredit $q3_hicredit0 locredit $q3_locredit0 offload 1
	fi
	if [ "$tbs_enabled0" -eq 1 ];
	then
		tc qdisc replace dev eth0 handle $q2_etf_handle0 parent $q2_cbs_handle0:3 etf clockid CLOCK_TAI delta 300000 offload skip_sock_check deadline_mode
		tc qdisc replace dev eth0 handle $q3_etf_handle0 parent $q3_cbs_handle0:4 etf clockid CLOCK_TAI delta 300000 offload skip_sock_check deadline_mode
	fi
fi

if [ "eth1" = "$interface" ];
then
	tc qdisc add dev eth1 handle $mqprio_handle1: parent root mqprio num_tc $num_tc1 map $mqprio_map1 queues 1@0 1@1 1@2 1@3 hw 0
	tc qdisc add dev eth1 clsact
	tc filter add dev eth1 egress prio 0 u32 match u16 0x88f7 0xffff at -2 action skbedit queue_mapping 1
	tc filter add dev eth1 egress prio 0 u32 match u16 0x22f0 0xffff at -2 action skbedit queue_mapping 2
	if [ $q2_idle_slope1 -ne 0 ] && [ $q2_send_slope1 -ne 0 ];
	then
		tc qdisc replace dev eth1 handle $q2_cbs_handle1 parent $mqprio_handle1:3 cbs idleslope $q2_idle_slope1 sendslope $q2_send_slope1 hicredit $q2_hicredit1 locredit $q2_locredit1 offload 1
	fi
	if [ $q3_idle_slope1 -ne 0 ] && [ $q3_send_slope1 -ne 0 ];
	then
		tc qdisc replace dev eth1 handle $q3_cbs_handle1 parent $mqprio_handle1:4 cbs idleslope $q3_idle_slope1 sendslope $q3_send_slope1 hicredit $q3_hicredit1 locredit $q3_locredit1 offload 1
	fi
	if [ "$tbs_enabled1" -eq 1 ];
	then
		tc qdisc replace dev eth1 handle $q2_etf_handle1 parent $q2_cbs_handle1:3 etf clockid CLOCK_TAI delta 300000 offload skip_sock_check deadline_mode
		tc qdisc replace dev eth1 handle $q3_etf_handle1 parent $q3_cbs_handle1:4 etf clockid CLOCK_TAI delta 300000 offload skip_sock_check deadline_mode
        fi
fi

echo "script loaded for QoS DONE" > $DUMP_TO_KMSG
exit 0
