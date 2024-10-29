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

echo systemds1 > /dev/kmsg

#dd if=/etc/init.d/setup_avtp_routing_le of=/dev/null
dd if=/etc/systemd/system/dbus-org.freedesktop.network1.service of=/dev/null
dd if=/lib/systemd/system/multi-user.target of=/dev/null
dd if=/lib/systemd/system/local-fs-pre.target of=/dev/null
ls -l /etc/systemd/system/local-fs-pre.target.wants
#dd if=/lib/systemd/system/init_data.service of=/dev/null
#dd if=/lib/systemd/system/init_audio.service of=/dev/null
echo systemdm1 > /dev/kmsg

dd if=/usr/lib/libweston-10.so.0.0.0 of=/dev/null
dd if=/usr/lib/libweston-10.so.0 of=/dev/null
#dd if=/usr/lib/libuhab.so of=/dev/null
dd if=/usr/libexec/weston-keyboard of=/dev/null
dd if=/usr/lib/libEGL_adreno.so of=/dev/null
dd if=/usr/share/fontconfig/conf.avail/40-nonlatin.conf of=/dev/null
dd if=/usr/share/fontconfig/conf.avail/50-user.conf of=/dev/null
dd if=/usr/share/fontconfig/conf.avail/10-hinting-none.conf of=/dev/null
dd if=/usr/share/fontconfig/conf.avail/10-no-sub-pixel.conf of=/dev/null

echo systemde1 > /dev/kmsg
