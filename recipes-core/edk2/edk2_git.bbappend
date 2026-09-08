FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append:gen5 = " file://00001-abl-support-append-cmdline-by-makefile.patch"

do_compile:append:gen5 () {
    # qclgvm-only variant: hardcodes OS Config to magic value 0xf so ABL
    # always selects the qclgvm dtb regardless of SoftSKU fuse state.
    rm -rf ${S}/out/Build
    oe_runmake -f makefile all "QCLGVM_OS_CFG=0xf"
    cp -f ${S}/../abl.elf ${WORKDIR}/abl-qclgvm.elf

    rm -rf ${S}/out/Build
    oe_runmake -f makefile all
}

do_deploy:append:gen5 () {
    mv ${S}/../abl.elf ${S}/../abl.elf.bak
    cp ${WORKDIR}/abl-qclgvm.elf ${S}/../abl.elf

    ${SIGNING_FUNCTION}

    install -m 0644 ${D}/boot/${PRODUCT}-abl.elf ${DEPLOYDIR}/${PRODUCT}-abl-qclgvm.elf
    mv ${S}/../abl.elf.bak ${S}/../abl.elf
}
