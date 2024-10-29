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

echo systemdsa > /dev/kmsg

dd if=/lib/systemd/systemd of=/dev/null
dd if=/lib/systemd/system/systemd-tmpfiles-setup.service of=/dev/null
dd if=/lib/systemd/system/systemd-tmpfiles-setup-dev.service of=/dev/null
dd if=/lib/systemd/system/systemd-timesyncd.service of=/dev/null
dd if=/lib/systemd/system/initrd-switch-root.service of=/dev/null
dd if=/lib/systemd/system/paths.target of=/dev/null
dd if=/lib/systemd/system/rc-local.service of=/dev/null
dd if=/lib/systemd/system/getty@.service of=/dev/null
dd if=/lib/systemd/system/systemd-tmpfiles-clean.service of=/dev/null
dd if=/lib/systemd/system/logrotate.timer of=/dev/null

echo systemdma > /dev/kmsg

dd if=/usr/lib/libmtdev.so.1.0.0 of=/dev/null
dd if=/usr/lib/libmtdev.so.1 of=/dev/null
dd if=/lib/libudev.so.1.7.3 of=/dev/null
dd if=/lib/libudev.so.1 of=/dev/null
dd if=/usr/lib/weston/desktop-shell.so of=/dev/null
dd if=/usr/lib/libsdmutils.so of=/dev/null
dd if=/usr/lib/libfreetype.so.6.18.1 of=/dev/null
dd if=/usr/lib/libfreetype.so.6 of=/dev/null

echo systemdea > /dev/kmsg
