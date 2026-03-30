#!/bin/sh
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: BSD-3-Clause-Clear

enable_log="${ENABLE_LOG:-0}"
log_path="${LOG_PATH:-/data}"
hcilog="$log_path/hcilog.cfa"

case "$1" in
    start)
        echo "########## Load bt modules ##########"

        # Reset BT
        rfkill block bluetooth
        rfkill unblock bluetooth

        # Load module + optional logging
        if [ "$enable_log" = "0" ]; then
            modprobe hci_uart
        else
            modprobe hci_uart && hcidump -w "$hcilog" &
        fi

        echo "########## done ##########"
        ;;

    stop)
        echo "########## Unload bt modules ##########"

        # Stop hcidump if running
        pkill -f "hcidump -w" 2>/dev/null || true

        # Unload HCI UART module
        modprobe -r hci_uart 2>/dev/null || true

        # Optionally block BT
        rfkill block bluetooth

        echo "########## cleanup done ##########"
        ;;

    *)
        echo "Usage: $0 {start|stop}"
        exit 1
        ;;
esac
