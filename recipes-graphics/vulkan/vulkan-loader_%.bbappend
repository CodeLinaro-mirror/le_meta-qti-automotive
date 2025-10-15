# We don't use mesa for vulkan loader, don't recommend it.
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append:sa8797 = " file://0001-Update-device-sorting-order.patch"
RRECOMMENDS:${PN} = ""
