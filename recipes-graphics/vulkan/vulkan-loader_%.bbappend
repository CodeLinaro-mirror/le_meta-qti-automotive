# We don't use mesa for vulkan loader, don't recommend it.
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append = " ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', ' file://0001-Remove-LOADER_ENABLE_LINUX_SORT-Macro-Definition.patch', ' ', d)}"
RRECOMMENDS:${PN} = ""
