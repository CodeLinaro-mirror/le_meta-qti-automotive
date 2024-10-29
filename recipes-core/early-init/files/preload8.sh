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

echo systemds7 > /dev/kmsg

#dd if=/lib/ld-2.31.so of=/dev/null
dd if=/lib/ld-linux-aarch64.so.1 of=/dev/null
dd if=/etc/systemd/system.conf of=/dev/null
#dd if=/etc/systemd/system/syslog.service of=/dev/null
dd if=/lib/systemd/system-generators/systemd-gpt-auto-generator of=/dev/null
dd if=/usr/lib/liblzma.so.5.2.5 of=/dev/null
dd if=/usr/lib/liblzma.so.5 of=/dev/null
dd if=/lib/systemd/system/resize-userdata.service of=/dev/null
dd if=/lib/systemd/system/systemd-fsck@.service of=/dev/null

echo systemdm7 > /dev/kmsg

dd if=/usr/lib/libgbm.so of=/dev/null
dd if=/usr/lib/libgsl.so of=/dev/null
dd if=/usr/lib/libwayland-cursor.so.0.20.0 of=/dev/null
dd if=/usr/lib/libwayland-cursor.so.0 of=/dev/null
dd if=/usr/lib/libtinyxml2_1.so of=/dev/null
dd if=/usr/lib/libeglSubDriverWayland.so of=/dev/null
dd if=/usr/share/fontconfig/conf.avail/69-unifont.conf of=/dev/null
dd if=/usr/share/fontconfig/conf.avail/80-delicious.conf of=/dev/null

echo systemde7 > /dev/kmsg
