# We don't use mesa for vulkan loader, don't recommend it.
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append:gen5 = " file://0001-Update-device-sorting-order.patch \
                        file://0002-Remove-LOADER_ENABLE_LINUX_SORT-Macro-Definition.patch \
                      "
RRECOMMENDS:${PN} = ""
