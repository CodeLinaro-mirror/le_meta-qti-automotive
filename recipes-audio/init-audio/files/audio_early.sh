# Copyright (c) 2020 The Linux Foundation. All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#   * Redistributions of source code must retain the above copyright
#     notice, this list of conditions and the following disclaimer.
#   * Redistributions in binary form must reproduce the above
#     copyright notice, this list of conditions and the following
#     disclaimer in the documentation and/or other materials provided
#     with the distribution.
#   * Neither the name of The Linux Foundation nor the names of its
#     contributors may be used to endorse or promote products derived
#     from this software without specific prior written permission.
#
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

# demo app for early audio to measure boot kpi
modprobe snd_event_dlkm
modprobe q6_notifier_dlkm
modprobe apr_dlkm
modprobe q6_dlkm
modprobe adsp_loader_dlkm
modprobe stub_dlkm
/bin/mount -o ro /dev/sde4 /firmware
/bin/mount -o ro /dev/mmcblk0p28 /firmware
/bin/echo 0 > /sys/module/subsystem_restart/parameters/enable_debug
/bin/echo 2 > /sys/kernel/boot_adsp/boot
modprobe platform_dlkm
nice -n -20 modprobe machine_dlkm
modprobe hdmi_dlkm

while true
do
    if cat /proc/asound/cards | grep "adp-star"
    then
        audio-nxp-auto
        amixer -c 0 cset iface=MIXER,name='TERT_TDM_RX_0 Channels' Two
        amixer -c 0 cset iface=MIXER,name='TERT_TDM_RX_0 Audio Mixer MultiMedia1' 1
        aplay -Dhw:0,0 /usr/share/sounds/alsa/Front_Left.wav
        amixer -c 0 cset iface=MIXER,name='TERT_TDM_RX_0 Audio Mixer MultiMedia1' 0
        break
    else
        sleep 0.02
    fi
done
