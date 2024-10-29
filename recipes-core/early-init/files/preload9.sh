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

echo systemds8 > /dev/kmsg

dd if=/lib/libz.so.1.2.11 of=/dev/null
dd if=/lib/libz.so.1 of=/dev/null
dd if=/lib/systemd/system/systemd-quotacheck.service of=/dev/null
dd if=/lib/systemd/system/initrd-cleanup.service of=/dev/null
dd if=/lib/systemd/system/initrd.target of=/dev/null
dd if=/lib/systemd/system/local-fs.target of=/dev/null
dd if=/lib/systemd/system/var-bluetooth.service of=/dev/null
#dd if=/lib/systemd/system/lxc-start.service of=/dev/null
dd if=/lib/systemd/system/iptables.service of=/dev/null
dd if=/lib/systemd/system/network-pre.target of=/dev/null
dd if=/lib/systemd/system/slices.target of=/dev/null
ls -l /lib/systemd/system/sysinit.target.wants

echo systemdm8 > /dev/kmsg

dd if=/usr/lib/libdrmutils.so of=/dev/null
dd if=/usr/lib/libffi.so.8.1.0 of=/dev/null
dd if=/usr/lib/libffi.so.8 of=/dev/null
dd if=/usr/lib/libcutils.so.0.0.0 of=/dev/null
dd if=/usr/lib/libcutils.so.0 of=/dev/null
dd if=/usr/lib/libpangoft2-1.0.so.0.5000.4 of=/dev/null
dd if=/usr/lib/libpangoft2-1.0.so.0 of=/dev/null
dd if=/usr/share/fontconfig/conf.avail/10-hinting-slight.conf of=/dev/null
dd if=/usr/share/fontconfig/conf.avail/45-generic.conf of=/dev/null
dd if=/usr/share/fontconfig/conf.avail/51-local.conf of=/dev/null
dd if=/usr/share/fontconfig/conf.avail/10-sub-pixel-bgr.conf of=/dev/null
dd if=/usr/share/fontconfig/conf.avail/10-sub-pixel-rgb.conf of=/dev/null

echo systemde8 > /dev/kmsg
