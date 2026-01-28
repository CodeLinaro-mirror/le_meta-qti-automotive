#!/bin/sh
# Copyright (c) 2020, The Linux Foundation. All rights reserved.

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

case $1/$2 in
  pre/*)
    echo "Entering into $2..."
    echo 1 > /sys/devices/system/cpu/cpu0/online
    echo 1 > /sys/devices/system/cpu/cpu1/online
    echo 1 > /sys/devices/system/cpu/cpu2/online
    echo 1 > /sys/devices/system/cpu/cpu3/online
    echo 1 > /sys/devices/system/cpu/cpu4/online
    echo 1 > /sys/devices/system/cpu/cpu5/online
    echo 1 > /sys/devices/system/cpu/cpu6/online
    echo 1 > /sys/devices/system/cpu/cpu7/online
    echo 1 > /sys/devices/system/cpu/cpu8/online
    echo 1 > /sys/devices/system/cpu/cpu9/online
    echo 1 > /sys/devices/system/cpu/cpu10/online
    echo 1 > /sys/devices/system/cpu/cpu11/online

    for i in /sys/devices/system/cpu/cpu*/cpuidle/state1/disable; do echo 0 > "$i"; done
    ;;
  post/*)
    echo "Exiting from $2..."

    for i in /sys/devices/system/cpu/cpu*/cpuidle/state1/disable; do echo 1 > "$i"; done

    echo 0 > /sys/devices/system/cpu/cpu0/online
    echo 0 > /sys/devices/system/cpu/cpu1/online
    echo 0 > /sys/devices/system/cpu/cpu2/online
    echo 0 > /sys/devices/system/cpu/cpu3/online
    echo 0 > /sys/devices/system/cpu/cpu4/online
    echo 0 > /sys/devices/system/cpu/cpu5/online
    echo 0 > /sys/devices/system/cpu/cpu6/online
    echo 0 > /sys/devices/system/cpu/cpu7/online
    echo 0 > /sys/devices/system/cpu/cpu8/online
    echo 0 > /sys/devices/system/cpu/cpu9/online
    echo 0 > /sys/devices/system/cpu/cpu10/online
    echo 0 > /sys/devices/system/cpu/cpu11/online
    ;;
esac
