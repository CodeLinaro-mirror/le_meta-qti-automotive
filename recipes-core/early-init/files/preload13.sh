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

echo systemdsc > /dev/kmsg

dd if=/lib/libmount.so.1.1.0 of=/dev/null
dd if=/lib/libmount.so.1 of=/dev/null
dd if=/lib/systemd/system/sockets.target of=/dev/null
dd if=/lib/systemd/system/systemd-networkd.socket of=/dev/null
dd if=/lib/systemd/system/systemd-journald-dev-log.socket of=/dev/null
dd if=/lib/systemd/system/systemd-journald.service of=/dev/null
dd if=/lib/systemd/system/syslog.socket of=/dev/null
dd if=/lib/systemd/system/systemd-journal-flush.service of=/dev/null
dd if=/lib/systemd/system/var-data.service of=/dev/null
dd if=/lib/systemd/system/var-build.prop.service of=/dev/null
dd if=/lib/systemd/system/systemd-networkd.service of=/dev/null
dd if=/lib/systemd/system/systemd-logind.service of=/dev/null
dd if=/lib/systemd/system/systemd-ask-password-wall.service of=/dev/null
dd if=/lib/systemd/system/systemd-networkd-wait-online.service of=/dev/null
dd if=/lib/systemd/systemd-journald of=/dev/null
#dd if=/etc/init.d/emac_dwc_eqos_start_stop_le of=/dev/null
ls -l /etc/systemd/system/local-fs.target.wants
ls -l /lib/systemd/system/runlevel2.target.wants
ls -l /lib/systemd/system/graphical.target.wants
dd if=/build.prop of=/dev/null

echo systemdmc > /dev/kmsg

dd if=/usr/lib/libgthread-2.0.so.0.7200.3 of=/dev/null
dd if=/usr/lib/libgthread-2.0.so.0 of=/dev/null
dd if=/usr/lib/libglib-2.0.so.0.7200.0 of=/dev/null
dd if=/usr/lib/libglib-2.0.so.0 of=/dev/null
#dd if=/usr/lib/lib_drm_fe.so of=/dev/null
dd if=/usr/lib/libhdr_tm.so of=/dev/null
dd if=/usr/lib/libsdm-color.so of=/dev/null
dd if=/usr/lib/libpango-1.0.so.0.5000.4 of=/dev/null
dd if=/usr/lib/libpango-1.0.so.0 of=/dev/null

echo systemdec > /dev/kmsg
