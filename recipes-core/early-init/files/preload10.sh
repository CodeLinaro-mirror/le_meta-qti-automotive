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

echo systemds9 > /dev/kmsg

dd if=/lib/systemd/system/systemd-fsck-root.service of=/dev/null
dd if=/lib/systemd/system/systemd-remount-fs.service of=/dev/null
dd if=/lib/systemd/system/initrd-parse-etc.service of=/dev/null
dd if=/lib/systemd/system/initrd-root-device.target of=/dev/null
dd if=/lib/systemd/system/usb.service of=/dev/null
dd if=/etc/systemd/system/thermal-engine.service of=/dev/null
dd if=/lib/systemd/system/getty.target of=/dev/null
dd if=/lib/systemd/system/serial-getty@.service of=/dev/null
ls -l /lib/systemd/system/sockets.target.wants
ls -l /etc/systemd/system/sockets.target.wants

echo systemdm9 > /dev/kmsg

dd if=/usr/lib/libbase.so.0.0.0 of=/dev/null
dd if=/usr/lib/libbase.so.0 of=/dev/null
dd if=/usr/lib/libsdedrm.so of=/dev/null
dd if=/usr/lib/liblog.so.0.0.0 of=/dev/null
dd if=/usr/lib/liblog.so.0 of=/dev/null
dd if=/usr/lib/libpangocairo-1.0.so.0.5000.4 of=/dev/null
dd if=/usr/lib/libpangocairo-1.0.so.0 of=/dev/null
dd if=/usr/lib/libexpat.so.1.8.7 of=/dev/null
dd if=/usr/lib/libexpat.so.1 of=/dev/null
dd if=/usr/share/fontconfig/conf.avail/20-unhint-small-vera.conf of=/dev/null
dd if=/usr/share/fontconfig/conf.avail/45-latin.conf of=/dev/null
dd if=/usr/share/fontconfig/conf.avail/60-generic.conf of=/dev/null
dd if=/usr/share/fontconfig/conf.avail/10-sub-pixel-vbgr.conf of=/dev/null
dd if=/usr/share/fontconfig/conf.avail/10-sub-pixel-vrgb.conf of=/dev/null

echo systemde9 > /dev/kmsg
