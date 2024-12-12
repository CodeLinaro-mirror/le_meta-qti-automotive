SUMMARY = "QCOM Linux Kernel"
DESCRIPTION = "QCOM Linux Kernel for QTI SoC"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

require recipes-kernel/linux/linux-qcom.inc

COMPATIBLE_MACHINE = "sa8775|sa8797|sa7255"

SRC_URI = "\
    ${PATH_TO_REPO}/kernel/kernel_platform/kernel/.git;protocol=${PROTO};destsuffix=kernel/kernel_platform/kernel;usehead=1 \
    file://generic.cfg \
    ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'file://selinux.cfg', '', d)} \
    ${@bb.utils.contains_any('VARIANT', 'perf user', '', 'file://devmem.cfg', d)} \
    ${@bb.utils.contains_any('VARIANT', 'perf user', 'file://perf.cfg', '', d)} \
    file://0001-QCLINUX-vfio-Disable-iommu_group_claim_dma_owner-tem.patch \
    file://0002-PENDING-soc-qcom-geni-se-Enable-QUPs-on-SA8255p-Qual.patch \
    file://0003-PENDING-serial-qcom-geni-Enable-Serial-on-SA8255p-pl.patch \
    file://0004-PENDING-i2c-qcom-geni-Enable-I2C-on-SA8255p-Qualcomm.patch \
    file://0005-PENDING-spi-geni-qcom-Enable-SPI-on-SA8255p-Qualcomm.patch \
    file://0006-PENDING-spi-geni-qcom-Enable-SPI-GSI-mode-for-SA8255.patch \
    file://0007-PENDING-scsi-ufs-qcom-Enable-sa8255p-platform.patch \
    file://0008-PENDING-phy-qcom-qmp-usb-Call-qmp_usb_remove-during-.patch \
    file://0009-PENDING-phy-qcom-qmp-usb-Add-support-for-SA8255P.patch \
    file://0010-PENDING-usb-dwc3-qcom-Add-support-for-sa8255p-for-qc.patch \
    file://0011-PENDING-phy-qcom-snps-femto-v2-Call-qcom_snps_hsphy_.patch \
    file://0012-PENDING-phy-qcom-snps-femto-v2-Add-support-for-SA825.patch \
    file://0001-FROMLIST-of-of_reserved_mem-Increase-limit-for-reser.patch \
    file://0013-net-stmmac-dwmac-qcom-ethqos-Enable-SCMI-ETH.patch \
    file://0014-PENDING-qcom-Add-sa7255p-compatibles-for-core-driver.patch \
    file://0015-PENDING-PCI-Add-Qualcomm-PCIe-ECAM-root-complex-driv.patch \
    file://0016-PENDING-ice-Enable-ICE-on-SA8255p-Qualcomm-platforms.patch \
    file://scm_adci/0001-QCLINUX-arm64-dts-qcom-sa8255p-Modify-correct-dt-nam.patch \
    file://scm_adci/0002-BACKPORT-FROMLIST-firmware-qcom-scm-Support-multiple.patch \
    file://scm_adci/0003-PENDING-firmware-qcom-scm-Add-support-for-WAITQ_WAKE.patch \
    file://scm_adci/0004-PENDING-firmware-qcom-scm-Selectively-skip-mutex-for.patch \
    file://scm_adci/0005-UPSTREAM-firmware-qcom-scm-Remove-QCOM_SMC_WAITQ_FLA.patch \
    file://scm_adci/0006-PENDING-firmware-qcom-scm-Introduce-new-locking-mech.patch \
    file://scm_adci/0007-BACKPORT-UPSTREAM-firmware-qcom-scm-add-support-for-.patch \
    file://scm_adci/0008-arm64-dts-qcom-sa8775p-Modify-correct-dt-name-for-SC.patch \
    file://scm_adci/0009-QCLINUX-arm64-dts-qcom-sa7255p-Modify-correct-dt-nam.patch \
"

S = "${WORKDIR}/kernel/kernel_platform/kernel"

KERNEL_CONFIG_FRAGMENTS:append = " ${WORKDIR}/generic.cfg"
KERNEL_CONFIG_FRAGMENTS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', '${WORKDIR}/selinux.cfg', '', d)}"
KERNEL_CONFIG_FRAGMENTS:append = " ${@bb.utils.contains_any('VARIANT', 'perf user', '', '${WORKDIR}/devmem.cfg', d)}"
