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

echo systemds4 > /dev/kmsg

dd if=/lib/systemd/system/sysinit.target of=/dev/null
dd if=/lib/systemd/system/weston.service of=/dev/null
dd if=/lib/systemd/system/memory-hotplug.service of=/dev/null
dd if=/lib/systemd/system/systemd-ask-password-console.service of=/dev/null
dd if=/lib/systemd/system/systemd-vconsole-setup.service of=/dev/null
dd if=/lib/systemd/system/initrd-switch-root.target of=/dev/null
#dd if=/lib/systemd/system/qrtr-ns.service of=/dev/null
dd if=/lib/systemd/system/pdmapper.service of=/dev/null
dd if=/lib/systemd/system/systemd-update-utmp-runlevel.service of=/dev/null
dd if=/lib/systemd/system/graphical.target of=/dev/null
#dd if=/lib/systemd/system/amfs.service of=/dev/null
dd if=/lib/systemd/system/ais_server.service of=/dev/null

echo systemdm4 > /dev/kmsg

dd if=/usr/lib/libinput.so.10.13.0 of=/dev/null
dd if=/usr/lib/libinput.so.10 of=/dev/null
dd if=/usr/lib/weston/desktop-shell.so of=/dev/null
dd if=/usr/lib/libhdrdynamic.so of=/dev/null
dd if=/usr/share/weston/terminal.png of=/dev/null
dd if=/usr/share/fontconfig/conf.avail/65-fonts-persian.conf of=/dev/null
ls -l /mnt

echo systemde4 > /dev/kmsg
