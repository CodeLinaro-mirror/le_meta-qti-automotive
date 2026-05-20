#!/bin/sh
#=============================================================================
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries. 
# SPDX-License-Identifier: BSD-3-Clause-Clear
#=============================================================================

echo "Thin_driver start"

DIR_THIN=/sys/devices/platform/soc@0/23040000.ethernet/net/eth0
exit_status=0

while true
do
	if [ -d "$DIR_THIN" ]
	then
		echo "Thin driver /sys/devices/.../eth0 is created."
		ifconfig eth0 up
		exit_status=$?
		if [ $exit_status -eq 0 ]
		then
			break
		else
			echo "ifconfig eth0 up failed (exit $exit_status), retrying..."
		fi
	fi
	sleep 0.05
done
echo "Thin_driver end"
