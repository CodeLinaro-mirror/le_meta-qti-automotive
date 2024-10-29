#!/bin/sh
# Copyright (c) 2020, The Linux Foundation. All rights reserved.

# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#    * Redistributions of source code must retain the above copyright
#      notice, this list of conditions and the following disclaimer.
#    * Redistributions in binary form must reproduce the above
#      copyright notice, this list of conditions and the following
#      disclaimer in the documentation and/or other materials provided
#      with the distribution.
#    * Neither the name of The Linux Foundation nor the names of its
#      contributors may be used to endorse or promote products derived
#      from this software without specific prior written permission.

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

echo systemds5 > /dev/kmsg

dd if=/etc/machine-id of=/dev/null
dd if=/lib/systemd/system/shutdown.target of=/dev/null
dd if=/lib/systemd/system/xinetd.service of=/dev/null
#dd if=/lib/librt-2.31.so of=/dev/null
dd if=/lib/librt.so.1 of=/dev/null
modprobe dwmac_qcom_eth &
dd if=/lib/systemd/system/initrd-udevadm-cleanup-db.service of=/dev/null
dd if=/lib/systemd/system/systemd-udev-settle.service of=/dev/null
dd if=/lib/systemd/system/basic.target of=/dev/null
#dd if=/lib/systemd/system/setup-network.service of=/dev/null
dd if=/lib/systemd/system/dbus.service of=/dev/null
dd if=/lib/systemd/system/crond.service of=/dev/null
dd if=/lib/systemd/system/rescue.service of=/dev/null
dd if=/lib/systemd/system/network.target of=/dev/null

echo systemdm5 > /dev/kmsg

dd if=/usr/lib/libpixman-1.so.0.40.0 of=/dev/null
dd if=/usr/lib/libpixman-1.so.0 of=/dev/null
dd if=/etc/xdg/weston/weston.ini of=/dev/null
ls /usr/share/X11/xkb
dd if=/usr/share/fontconfig/conf.avail/30-metric-aliases.conf of=/dev/null
dd if=/usr/share/fontconfig/conf.avail/49-sansserif.conf of=/dev/null

echo systemde5 > /dev/kmsg
