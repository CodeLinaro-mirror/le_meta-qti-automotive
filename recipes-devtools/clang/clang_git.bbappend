do_install:append:class-native () {

    # FIXME: Workaround for cfi_blacklist.txt check after CONFIG_CFI_CLANG is enabled
    if [ ! -f ${D}${nonarch_libdir}/clang/${PV}/share/cfi_blacklist.txt ]; then
        install -d ${D}${nonarch_libdir}/clang/${PV}/share
        touch ${D}${nonarch_libdir}/clang/${PV}/share/cfi_blacklist.txt
    fi
}

# Disable -fPIE and -pie on Linux
EXTRA_OECMAKE:remove:class-target = "-DCLANG_DEFAULT_PIE_ON_LINUX=ON"
EXTRA_OECMAKE:remove:class-nativesdk = "-DCLANG_DEFAULT_PIE_ON_LINUX=ON"
EXTRA_OECMAKE:remove:class-native = "-DCLANG_DEFAULT_PIE_ON_LINUX=ON"

# Disable libgcc unwind library to use
EXTRA_OECMAKE:remove:class-target = "-DCLANG_DEFAULT_UNWINDLIB=libgcc"
EXTRA_OECMAKE:remove:class-nativesdk = "-DCLANG_DEFAULT_UNWINDLIB=libgcc"
EXTRA_OECMAKE:remove:class-native = "-DCLANG_DEFAULT_UNWINDLIB=libgcc"
