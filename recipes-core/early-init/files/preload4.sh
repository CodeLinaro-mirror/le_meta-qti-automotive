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

echo systemds3 > /dev/kmsg

dd if=/etc/hostname of=/dev/null
dd if=/lib/systemd/system-generators/systemd-sysv-generator of=/dev/null
#dd if=/lib/libpthread-2.31.so of=/dev/null
dd if=/lib/libpthread.so.0 of=/dev/null
dd if=/lib/systemd/system/initrd-fs.target of=/dev/null
dd if=/lib/systemd/system/initrd-root-fs.target of=/dev/null
dd if=/lib/systemd/system/rescue.target of=/dev/null
dd if=/lib/systemd/system/ctrl-alt-del.target of=/dev/null
dd if=/lib/systemd/system/reboot.target of=/dev/null
dd if=/lib/systemd/system/emergency.service of=/dev/null
dd if=/lib/systemd/system/getty-pre.target of=/dev/null
#dd if=/lib/systemd/system/emac_dwc_eqos.service of=/dev/null
dd if=/lib/systemd/system/dev-mqueue.mount of=/dev/null
dd if=/lib/systemd/system/dev-hugepages.mount of=/dev/null

echo systemdm3 > /dev/kmsg

dd if=/usr/lib/libinput.so.10.13.0 of=/dev/null
dd if=/usr/lib/libinput.so.10 of=/dev/null
dd if=/usr/lib/libweston-10/drm-backend.so of=/dev/null
dd if=/usr/lib/libpng16.so.16.37.0 of=/dev/null
dd if=/usr/lib/libdisplayqos.so of=/dev/null
dd if=/etc/fonts/fonts.conf of=/dev/null
dd if=/usr/share/fontconfig/conf.avail/65-nonlatin.conf of=/dev/null

echo systemde3 > /dev/kmsg
