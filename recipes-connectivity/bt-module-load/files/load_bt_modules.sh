#!/bin/sh
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: BSD-3-Clause-Clear

enable_log="${ENABLE_LOG:-0}"
log_path="${LOG_PATH:-/data}"

# Parse optional flags
while getopts "l:p:" opt; do
    case "$opt" in
        l) enable_log="$OPTARG" ;;
        p) log_path="$OPTARG" ;;
        *) echo "Usage: $0 [-l enable_log] [-p log_path] {start|stop}"; exit 1 ;;
    esac
done
shift $((OPTIND - 1))

hcilog="$log_path/hcilog.btsnoop"

case "$1" in
    start)
        echo "########## Load bt modules ##########"

        # Reset BT
        rfkill block bluetooth
        rfkill unblock bluetooth

        # Load module + optional logging
        if [ "$enable_log" = "1" ]; then
            echo "########## start btmon ##########"
            btmon -w "$hcilog" > /dev/null 2>&1 &
        fi

        modprobe hci_uart

        echo "########## done ##########"
        ;;

    stop)
        echo "########## Unload bt modules ##########"

        # Stop hcidump if running
        pkill -f "btmon -w" 2>/dev/null || true

        # Unload HCI UART module
        modprobe -r hci_uart 2>/dev/null || true

        # Optionally block BT
        rfkill block bluetooth

        echo "########## cleanup done ##########"
        ;;

    *)
        echo "Usage: $0 [-l enable_log] [-p log_path] {start|stop}"
        exit 1
        ;;
esac
