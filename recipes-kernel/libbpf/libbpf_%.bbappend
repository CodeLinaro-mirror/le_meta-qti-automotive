EXTRA_OEMAKE:append:class-native = " UAPIDIR=${includedir}"

do_install:append:class-native() {
    oe_runmake install_uapi_headers
}

BBCLASSEXTEND = "native nativesdk"
