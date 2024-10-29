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

echo systemds0 > /dev/kmsg

echo "early-init:preload clkscale, autohibern8  disable"
#echo 0 > /sys/bus/platform/devices/1d84000.ufshc/clkscale_enable
echo 0 > /sys/bus/platform/devices/1d84000.ufshc/auto_hibern8

dd if=/lib/systemd/system/systemd-update-utmp.service of=/dev/null
dd if=/lib/systemd/system/umount.target of=/dev/null
dd if=/lib/systemd/system/systemd-journal-catalog-update.service of=/dev/null
dd if=/lib/systemd/system/systemd-hwdb-update.service of=/dev/null
dd if=/lib/systemd/system/systemd-ask-password-console.path of=/dev/null
dd if=/lib/systemd/system/sys-kernel-config.mount of=/dev/null
dd if=/lib/systemd/system/sys-fs-fuse-connections.mount of=/dev/null
dd if=/lib/systemd/system/ip6tables.service of=/dev/null
dd if=/lib/systemd/system/autovt@.service of=/dev/null
dd if=/lib/systemd/system/init_post_boot.service of=/dev/null
dd if=/lib/systemd/system/adbd.service of=/dev/null
dd if=/lib/systemd/system/acdb_loader.service of=/dev/null
dd if=/lib/systemd/system/ab-updater.service of=/dev/null
dd if=/lib/systemd/system/persist-prop.service of=/dev/null

echo systemdm0 > /dev/kmsg

dd if=/usr/bin/weston of=/dev/null
dd if=/usr/lib/libion.so.0.0.0 of=/dev/null
dd if=/usr/lib/libion.so.0 of=/dev/null
dd if=/usr/libexec/weston-desktop-shell of=/dev/null
dd if=/usr/lib/libEGL.so.1.0.0 of=/dev/null
dd if=/usr/lib/libEGL.so.1.0 of=/dev/null
dd if=/usr/lib/libEGL.so.1 of=/dev/null
dd if=/usr/lib/libweston-10/gl-renderer.so of=/dev/null
dd if=/etc/fonts/conf.d/30-liberation-aliases.conf of=/dev/null
dd if=/usr/share/fontconfig/conf.avail/10-hinting-full.conf of=/dev/null
dd if=/usr/share/fontconfig/conf.avail/10-hinting-medium.conf of=/dev/null

echo systemde0 > /dev/kmsg
