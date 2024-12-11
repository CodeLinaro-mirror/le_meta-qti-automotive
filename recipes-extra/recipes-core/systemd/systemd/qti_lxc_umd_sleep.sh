#!/bin/sh
# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear
case $1/$2 in
  pre/*)
    echo "Entering into LXC UMD $2..."

    systemctl stop sail-mailbox
    systemctl stop video-driver.service
    systemctl stop weston
    sleep 2
    systemctl stop openwfd_server_@0.service

    ;;
  post/*)
    echo "Exiting from LXC UMD $2..."

    systemctl restart openwfd_server_@0.service
    systemctl restart weston
    sleep 1
    systemctl restart video-driver.service
    systemctl restart sail-mailbox
    ;;
esac
