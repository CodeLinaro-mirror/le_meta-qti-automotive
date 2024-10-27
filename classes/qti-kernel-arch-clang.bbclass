#Copyright (c) 2022 Qualcomm Innovation Center, Inc. All rights reserved.
#SPDX-License-Identifier: BSD-3-Clause-Clear

#Due to inherit kernel-arch.bbclass, switching to clang compilation requires override the necessary toolchain environment variables
LD:toolchain-clang = "${HOST_PREFIX}ld.lld${TOOLCHAIN_OPTIONS} ${HOST_LD_ARCH}"
KERNEL_CC:toolchain-clang = "${CCACHE}${HOST_PREFIX}clang -target ${HOST_SYS} ${HOST_CC_KERNEL_ARCH} "
KERNEL_LD:toolchain-clang = "${CCACHE}${HOST_PREFIX}ld.lld ${HOST_LD_KERNEL_ARCH}"
KERNEL_AR:toolchain-clang = "${CCACHE}${HOST_PREFIX}llvm-ar ${HOST_AR_KERNEL_ARCH}"
# Pass LLVM_IAS=1 to ensure Clang use cross toolchain compatible for LLVM 14.x.x version.
EXTRA_OEMAKE:append:toolchain-clang = " LLVM_IAS=1"
TOOLCHAIN = "clang"

