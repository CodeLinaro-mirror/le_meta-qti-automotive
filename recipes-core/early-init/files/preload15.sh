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

echo systemdse > /dev/kmsg

dd if=/lib/systemd/system/dbus.socket of=/dev/null
dd if=/lib/systemd/system/systemd-udevd-control.socket of=/dev/null
dd if=/lib/systemd/system/systemd-udev-trigger.service of=/dev/null
dd if=/lib/systemd/system/time-set.target of=/dev/null
dd if=/lib/systemd/system/systemd-sysusers.service of=/dev/null
dd if=/lib/systemd/system/systemd-sysctl.service of=/dev/null
dd if=/lib/systemd/system/var-smack-accesses.d.service of=/dev/null
dd if=/lib/systemd/system/var-misc-wifi.service of=/dev/null
dd if=/lib/systemd/system/systemd-user-sessions.service of=/dev/null
dd if=/lib/systemd/system/nss-user-lookup.target of=/dev/null
#dd if=/lib/systemd/system/synergy.service of=/dev/null
dd if=/lib/systemd/system/subsystem-ramdump.service of=/dev/null
dd if=/lib/systemd/system/servicemanager.service of=/dev/null
dd if=/usr/bin/servicemanager of=/dev/null
#dd if=/lib/systemd/system/lxc-init.service of=/dev/null
dd if=/lib/libcap.so.2.63 of=/dev/null
dd if=/lib/libcap.so.2 of=/dev/null
ls -l /lib/systemd/system.conf.d
ls -l /lib/systemd/system/local-fs.target.wants
ls -l /lib/systemd/system/runlevel5.target.wants
ls -l /lib/systemd/system/rescue.target.wants

echo systemdme > /dev/kmsg

dd if=/usr/lib/libdmabufheap.so.0.0.0 of=/dev/null
dd if=/usr/lib/libdmabufheap.so.0 of=/dev/null
dd if=/usr/lib/libdrm.so.2.4.0 of=/dev/null
dd if=/usr/lib/libdrm.so.2 of=/dev/null
dd if=/usr/lib/libdisplaydebug.so of=/dev/null
dd if=/usr/lib/libadreno_utils.so of=/dev/null
dd if=/usr/lib/libutils.so.0.0.0 of=/dev/null
dd if=/usr/lib/libutils.so.0 of=/dev/null
dd if=/usr/lib/libcairo.so.2.11600.0 of=/dev/null
dd if=/usr/lib/libcairo.so.2 of=/dev/null
dd if=/usr/lib/libgobject-2.0.so.0.7200.0 of=/dev/null
dd if=/usr/lib/libgobject-2.0.so.0 of=/dev/null
dd if=/usr/lib/libharfbuzz.so.0.40001.0 of=/dev/null
dd if=/usr/lib/libharfbuzz.so.0 of=/dev/null

echo systemdee > /dev/kmsg
