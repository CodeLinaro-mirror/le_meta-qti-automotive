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

echo systemdsd > /dev/kmsg

#dd if=/lib/libc-2.31.so of=/dev/null
dd if=/lib/libc.so.6 of=/dev/null
dd if=/lib/libm.so.6 of=/dev/null
dd if=/lib/systemd/system/systemd-update-done.service of=/dev/null
dd if=/lib/systemd/system/systemd-udevd.service of=/dev/null
dd if=/lib/systemd/system/systemd-udevd-kernel.socket of=/dev/null
dd if=/lib/systemd/system/systemd-random-seed.service of=/dev/null
dd if=/lib/systemd/system/systemd-modules-load.service of=/dev/null
dd if=/lib/systemd/system/systemd-machine-id-commit.service of=/dev/null
dd if=/lib/systemd/system/remote-fs-pre.target of=/dev/null
dd if=/lib/systemd/system/var-usb.service of=/dev/null
dd if=/lib/systemd/system/systemd-resolved.service of=/dev/null
dd if=/lib/systemd/system/nss-lookup.target of=/dev/null
dd if=/lib/systemd/system/strongswan.service of=/dev/null
dd if=/lib/systemd/system/network-online.target of=/dev/null
#dd if=/lib/systemd/system/rngd.service of=/dev/null
#dd if=/lib/systemd/system/msm-bus.service of=/dev/null
#ls -l /etc/systemd/system/basic.target.wants
ls -l /lib/systemd/system/multi-user.target.wants
ls -l /lib/systemd/system/runlevel1.target.wants
ls -l /etc/systemd/system/network-online.target.wants

echo systemdmd > /dev/kmsg

dd if=/usr/lib/libllvm-qgl.so of=/dev/null
dd if=/usr/lib/libsync.so.0.0.0 of=/dev/null
dd if=/usr/lib/libsync.so.0 of=/dev/null
dd if=/usr/lib/libstdc++.so.6.0.29 of=/dev/null
dd if=/usr/lib/libstdc++.so.6 of=/dev/null
dd if=/usr/lib/libpcre.so.1.2.13 of=/dev/null
dd if=/usr/lib/libpcre.so.1 of=/dev/null
dd if=/usr/lib/libsdmcore.so of=/dev/null
dd if=/usr/lib/libsdmextension.so of=/dev/null
dd if=/usr/lib/libfontconfig.so.1.12.0 of=/dev/null

echo systemded > /dev/kmsg
