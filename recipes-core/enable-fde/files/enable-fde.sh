#!/bin/sh

#Copyright (c) 2019, 2021 The Linux Foundation. All rights reserved.
#
#Redistribution and use in source and binary forms, with or without
#modification, are permitted provided that the following conditions are
#met:
#    * Redistributions of source code must retain the above copyright
#      notice, this list of conditions and the following disclaimer.
#    * Redistributions in binary form must reproduce the above
#      copyright notice, this list of conditions and the following
#      disclaimer in the documentation and/or other materials provided
#      with the distribution.
#    * Neither the name of The Linux Foundation nor the names of its
#      contributors may be used to endorse or promote products derived
#      from this software without specific prior written permission.
#
#THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
#WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
#MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
#ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
#BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
#CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
#SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
#BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
#WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
#OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
#IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

# Changes from Qualcomm Innovation Center are provided under the following license:

# Copyright (c) 2022 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear

fde_setup () {
    /usr/bin/fde_ctl -p $DEV_NAME
    rc=$?

    case $rc in
        1)  #First run or user_pass has changed or 'ssd' got busted, seed data.
            echo "/data's ICE key provisioned, seed the /data directory."
            seed_data;
            if [ $? -ne 0 ] ; then
                echo "failed to seed data partition"
                return 1
            fi
            if [ ! -e /persist/.data_formatted ]; then
                touch /persist/.data_formatted && sync;
                if [ $? -ne 0 ] ; then
                    echo "failed to create data formatted file"
                    return 1
                fi
            fi
            return 0
            ;;
        0)  echo "Successfully set the FDE key for /data"
            if [ ! -e /persist/.data_formatted ]; then
                touch /persist/.data_formatted && sync;
                if [ $? -ne 0 ] ; then
                    echo "failed to create data formatted file"
                    return 1
                fi
                seed_data;
                if [ $? -ne 0 ] ; then
                    echo "failed to seed data partition"
                    return 1
                fi
            fi
            return 0
            ;;
        *)  echo "Failed to set input key for $DEV_PATH/data, rc=${rc}"
            return 1;
            ;;
    esac
}

seed_data() {
    echo "All data within /data would be lost"
    /sbin/mkfs.ext4  $DEV_PATH/$DEV_NAME
    if [ $? -ne 0 ] ; then
        echo " failed to format data"
        return 1
    fi
    return 0
}

DEV_PATH="/dev";
DEV_NAME="vdb";
fde_setup
if [ $? -ne 0 ] ; then
   exit 1
fi

exit 0
