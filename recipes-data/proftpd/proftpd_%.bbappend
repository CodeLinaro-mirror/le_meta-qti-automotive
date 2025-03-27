FILESEXTRAPATHS:append := ":${THISDIR}/files"

SRC_URI:append = " file://disable-reverse-DNS-check.patch \
                   file://enable-RootLogin.patch"
