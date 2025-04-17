# Copyright (c) 2019, The Linux Foundation. All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#     * Redistributions of source code must retain the above copyright
#       notice, this list of conditions and the following disclaimer.
#     * Redistributions in binary form must reproduce the above
#       copyright notice, this list of conditions and the following
#       disclaimer in the documentation and/or other materials provided
#       with the distribution.
#     * Neither the name of The Linux Foundation nor the names of its
#       contributors may be used to endorse or promote products derived
#       from this software without specific prior written permission.
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
#

# Changes from Qualcomm Innovation Center, Inc. are provided under the following license:
# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear

# Skip get_checksum_file_list function detection
# do_fetch[file-checksums] = "${@get_lic_checksum_file_list(d)}"

python __anonymous(){
    import re

    pre_pathname = d.getVar('PATH_TO_REPO')
    src_uri_list = d.getVar('SRC_URI').replace("\t"," ").split(" ")
    new_src_uri_list = []
    need_change = False
    for srcuri in src_uri_list:
        if (srcuri.find(pre_pathname) < 0) and (srcuri.find("protocol=file") < 0):
            if srcuri.strip() != "":
                new_src_uri_list.append(srcuri)
            continue
        new_srcuri = srcuri
        try:
            new_srcuri = "file://%s" % re.findall(r"^(.+?);", srcuri.strip())[0].replace(pre_pathname+"/","").replace(".git","")
            need_change = True
        except:
            bb.warn("[qfile debug] Change SRC_URI failed")
        new_src_uri_list.append(new_srcuri)

    extra_srcrev_lists = ["SRCREV_kernel",
                          "SRCREV_clang",
                          "SRCREV_build-kernel",
                          "SRCREV_build-tools",
                          "SRCREV_kernel-build-tools",
                          "SRCREV_ndk",
                          "SRCREV_glibc",
                          "SRCREV_qcacld",
                          "SRCREV_qca-wifi-host-cmn",
                          "SRCREV_fw-api",
                          "SRCREV_wlan",
                          "SRCREV_devicetree",
                          "SRCREV_camx-common",
                          "SRCREV_camx-api",
                          "SRCREV_camx-test",
                          "SRCREV_cdk-qcx",
                          "SRCREV_qcx",
                          "SRCREV_cdk-noship",
                          "SRCREV_audio-kernel-ar",
                          "SRCREV_audio-kernel",
                          "SRCREV_dspkernel",
                          "SRCREV_platformkernel"]

    if need_change:
        d.prependVar("FILESPATH", "${SRC_DIR_ROOT}/:")
        new_src_uri = " ".join(new_src_uri_list)
        # Workaround to skip AUTOREV/SRCPV set too late issue.
        d.setVar("__BBAUTOREV_ACTED_UPON", True)
        d.setVar("SRC_URI", new_src_uri)
        d.setVar("SRCREV", '')

        # Some recipes have multiple SRCREV, set them to empty.
        for rev in extra_srcrev_lists:
            d.setVar(rev, "")
}
