#!/bin/sh
# Copyright (c) 2019 The Linux Foundation. All rights reserved.
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
#
# Changes from Qualcomm Innovation Center are provided under the following license:
#
# Copyright (c) 2022 Qualcomm Innovation Center, Inc. All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted (subject to the limitations in the
# disclaimer below) provided that the following conditions are met:
#
#    * Redistributions of source code must retain the above copyright
#      notice, this list of conditions and the following disclaimer.
#
#    * Redistributions in binary form must reproduce the above
#      copyright notice, this list of conditions and the following
#      disclaimer in the documentation and/or other materials provided
#      with the distribution.
#
#    * Neither the name of Qualcomm Innovation Center, Inc. nor the names of its
#      contributors may be used to endorse or promote products derived
#      from this software without specific prior written permission.
#
# NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE
# GRANTED BY THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT
# HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
# WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
# MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
# IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
# ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
# DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
# GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
# INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
# IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
# OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
# IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
#

install_module() {
	modprobe $1 || modprobe -d /vendor $1;
	echo 1 > /sys/kernel/cnss_0/fs_ready
}

echo "##########Trying to load wlanhost driver ##########"
n=0
while [ $n -le 5 ]
	do
	if (lspci -k|grep cnss_pci);then
		if [ "$(lspci -k | grep 1102)" ] || [ "$(lspci -n | grep 1102)" ]; then
			echo "##########load qca6595#############"
			if [ -f /firmware/image/qcn7605/amss.bin ];then
				install_module qca6595
			else
				echo "##########Error! QCA6595 FW is not available!#####"
			fi
		elif [ "$(lspci -k | grep 003e)" ] || [ "$(lspci -n | grep 003e)" ] || [ "$(lspci -k|grep QCA6174)" ];then
			echo "##########load qca6574#############"
			if [ -f /firmware/image/qca6174/qwlan30.bin ];then
				install_module qca6574
			else
				echo "##########Error! QCA6574 FW is not available!#####"
			fi
		elif [ "$(lspci -k | grep 1101)" ] || [ "$(lspci -n | grep 1101)" ] || [ "$(lspci -k|grep QCA6390)" ];then
			echo "##########load qca6696#############"
			if [ -f /firmware/image/qca6390/amss20.bin ];then
				install_module qca6696
			else
				echo "##########Error! QCA6696 FW is not available!#####"
			fi
		elif [ "$(lspci -k | grep 1103)" ] || [ "$(lspci -n | grep 1103)" ];then
			echo "##########load qca6698#############"
			if [ -f /firmware/image/qca6490/amss20.bin ];then
				install_module qca6698
			else
				echo "##########Error! QCA6698 FW is not available!#####"
			fi
		elif [ "$(lspci -k | grep 1107)" ] || [ "$(lspci -n | grep 1107)" ];then
			echo "##########load qca6797#############"
			if [ -f /firmware/image/kiwi/amss20.bin ];then
				install_module qca6797
			else
				echo "##########Error! QCA6797 FW is not available!#####"
			fi
		else
			echo "##########load default wlan########"
			install_module wlan
		fi
		break
	fi
	n=$((n + 1))
	echo "Retry loading wlan @$n"
	sleep 1
done
echo "##########Load wlanhost driver done################"

