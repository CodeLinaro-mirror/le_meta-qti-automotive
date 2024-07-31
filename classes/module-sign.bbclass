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

# Changes from Qualcomm Innovation Center, Inc. are provided under the following license:
# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear

MODULES_PATH ?= "${PKGDEST}/${PN}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/"

do_module_signing() {
    if grep -q '^CONFIG_MODULE_SIG=y' ${STAGING_KERNEL_BUILDDIR}/.config; then
        module_sig_pri_key=`grep '^CONFIG_MODULE_SIG_KEY=' ${STAGING_KERNEL_BUILDDIR}/.config | cut -f2 -d'"'`
        module_sig_pub_key=`echo $module_sig_pri_key | sed -e s/.pem/.x509/`
        module_sig_hash=`cat ${STAGING_KERNEL_BUILDDIR}/.config | grep CONFIG_MODULE_SIG_HASH | cut -d'=' -f2 | sed 's/\"//g'`

        for i in $(find ${MODULES_PATH} -name "*.ko"); do
            ${STAGING_KERNEL_BUILDDIR}/scripts/sign-file ${module_sig_hash} ${STAGING_KERNEL_BUILDDIR}/${module_sig_pri_key} ${STAGING_KERNEL_BUILDDIR}/${module_sig_pub_key} ${i}
            bbnote "Signing ${PN} module ${i}"
        done
    else
        bbnote "${PN} module is not being signed"
    fi
}

addtask module_signing after do_package before do_packagedata
