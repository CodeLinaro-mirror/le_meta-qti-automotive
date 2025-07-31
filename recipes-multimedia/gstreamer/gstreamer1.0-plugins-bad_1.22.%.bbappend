FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}/1.22.12:"
SRC_URI:append = " file://0001-gstwaylandsink-add-P010_10LE-support-statement.patch \
                   file://0002-waylandsink-add-ubwc-modifier-and-zwp_linux_dmabuf_v.patch \
                 "
