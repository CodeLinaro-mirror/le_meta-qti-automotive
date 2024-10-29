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

echo systemdsf > /dev/kmsg

dd if=/lib/systemd/system/systemd-initctl.service of=/dev/null
dd if=/lib/systemd/system/tmp.mount of=/dev/null
dd if=/lib/systemd/system/time-sync.target of=/dev/null
dd if=/lib/systemd/system/swap.target of=/dev/null
dd if=/lib/systemd/system/remote-fs.target of=/dev/null
dd if=/lib/systemd/system/var-allplay.service of=/dev/null
#dd if=/lib/systemd/system/var-adb_devid.service of=/dev/null
dd if=/lib/systemd/system/user.slice of=/dev/null
dd if=/lib/systemd/system/systemd-ask-password-wall.path of=/dev/null
#dd if=/lib/systemd/system/strongswan-starter.service of=/dev/null
dd if=/etc/systemd/system/sfsconfig.service of=/dev/null
dd if=/lib/systemd/system/timers.target of=/dev/null
dd if=/lib/systemd/system/systemd-tmpfiles-clean.timer of=/dev/null
dd if=/lib/systemd/system/systemd-journald.socket of=/dev/null
#dd if=/etc/default/rng-tools of=/dev/null
dd if=/etc/systemd/journald.conf of=/dev/null
dd if=/lib/systemd/journald.conf.d/00-systemd-conf.conf of=/dev/null
ls -l /etc/systemd/system/multi-user.target.wants
ls -l /etc/systemd/system/getty.target.wants
ls -l /etc/systemd/system/timers.target.wants
ls -l /lib/systemd/system/timers.target.wants

echo systemdmf > /dev/kmsg

#21.5MB libGLESv2_adreno
dd if=/usr/lib/libGLESv2.so.2 of=/dev/null
dd if=/usr/lib/libGLESv2.so.2.0 of=/dev/null
dd if=/usr/lib/libGLESv2.so.2.0.0 of=/dev/null
dd if=/usr/lib/libGLESv2_adreno.so of=/dev/null

echo systemdef > /dev/kmsg
