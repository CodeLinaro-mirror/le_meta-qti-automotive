SUMMARY = "Security configiure file"
DESCRIPTION = "The secursey config file is used for QMI(Qualcomm Message Interface) \
               client running as non-root users. The file contains a list of security rules, which \
               is defined as [QMI_Service_ID:QMI_Instance_ID:QMI_Client_Group_ID]. \
               The irsc_util(IPC Router Security Utilty) binary reads this file and feed the \
               security rules to the IPC Router."
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "file://sec_config"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -m 0444 ${WORKDIR}/sec_config -D ${D}${sysconfdir}/sec_config
}
