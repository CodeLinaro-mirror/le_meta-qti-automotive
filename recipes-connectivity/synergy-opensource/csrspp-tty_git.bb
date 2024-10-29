SUMMARY = "QTI Bluetooth SPP TTY driver for AGL Platform"
DESCRIPTION = "Spp tty is part of Synergy BT Stack\
distribution which implements tty driver for SPP profile."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += "virtual/kernel"

SRC_URI = "${PATH_TO_REPO}/synergy/synergy-opensource/.git;protocol=${PROTO};destsuffix=synergy/synergy-opensource;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/synergy/synergy-opensource/platform/msm/spp"

inherit module module-sign qti-kernel-arch-clang

PACKAGE_ARCH = "${MACHINE_ARCH}"

_MODNAME = "csrspp-tty"
PROVIDES_NAME = "kernel-module-${_MODNAME}"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*"

RPROVIDES:${PN} += "${PROVIDES_NAME}-${KERNEL_VERSION}"
