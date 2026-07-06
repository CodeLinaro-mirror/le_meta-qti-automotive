FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append = " file://0001-lat_rpc-fix-uninitialized-state.server-in-S-shutdown.patch"
