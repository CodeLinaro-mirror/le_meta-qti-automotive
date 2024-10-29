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

echo systemdsb > /dev/kmsg

dd if=/lib/systemd/libsystemd-shared-250.so of=/dev/null
dd if=/lib/systemd/system/systemd-journald-audit.socket of=/dev/null
dd if=/lib/systemd/system/systemd-initctl.socket of=/dev/null
dd if=/lib/systemd/system/sshd.socket of=/dev/null
dd if=/lib/systemd/system/qseecomd.service of=/dev/null
dd if=/etc/initscripts/qseecomd of=/dev/null
dd if=/lib/systemd/system/connman.service of=/dev/null
#dd if=/etc/systemd/system/chgrp-diag.service of=/dev/null
dd if=/etc/systemd/system/dbus-org.freedesktop.resolve1.service of=/dev/null
dd if=/etc/systemd/system/dbus-org.freedesktop.timesync1.service of=/dev/null
dd if=/sbin/leprop-service of=/dev/null

echo systemdmb > /dev/kmsg

dd if=/usr/lib/libevdev.so.2.3.0 of=/dev/null
dd if=/usr/lib/libevdev.so.2 of=/dev/null
dd if=/usr/lib/libwayland-client.so.0.20.0 of=/dev/null
dd if=/usr/lib/libwayland-client.so.0 of=/dev/null
dd if=/usr/lib/libhardware.so.0.0.0 of=/dev/null
dd if=/usr/lib/libhardware.so.0 of=/dev/null
dd if=/usr/lib/libjpeg.so.62.3.0 of=/dev/null
dd if=/usr/lib/libjpeg.so.62 of=/dev/null

echo systemdeb > /dev/kmsg
