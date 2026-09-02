#!/bin/sh
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: BSD-3-Clause-Clear

modprobe ipv6
modprobe phylink
modprobe marvell
modprobe aquantia
modprobe phy_qcom_sgmii_eth
modprobe pcs_xpcs
modprobe stmmac
modprobe stmmac_platform
modprobe dwmac_qcom_ethqos
