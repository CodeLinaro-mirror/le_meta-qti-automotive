FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append:gen5 = " file://00001-abl-support-append-cmdline-by-makefile.patch"

do_compile:append:gen5 () {
    # Build the single-lv-gvm variant first (with extra cmdline), then rebuild
    # the default without it, this is workaround for not having softsku for
    # single-lv-gvm.
    rm -rf ${S}/out/Build
    oe_runmake -f makefile all "EXTRA_STATIC_CMDLINE=gvmconfig=single-lv-gvm"
    cp -f ${S}/../abl.elf ${WORKDIR}/abl-single-lv-gvm.elf

    rm -rf ${S}/out/Build
    oe_runmake -f makefile all
}

do_deploy:append:gen5 () {
    mv ${S}/../abl.elf ${S}/../abl.elf.bak
    cp ${WORKDIR}/abl-single-lv-gvm.elf ${S}/../abl.elf

    ${SIGNING_FUNCTION}

    install -m 0644 ${D}/boot/${PRODUCT}-abl.elf ${DEPLOYDIR}/${PRODUCT}-abl-single-lv-gvm.elf
    mv ${S}/../abl.elf.bak ${S}/../abl.elf
}
