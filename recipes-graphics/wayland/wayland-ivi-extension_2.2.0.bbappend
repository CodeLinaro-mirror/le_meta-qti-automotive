FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://0001-Fix-link-error-of-variables-multiple-definition.patch \
    file://0001-Change-header-file-path-to-adapt-to-weston-10.0.0.patch \
"

SRCREV = "cfdb7a7402a12600daedddb46da2e360359cc05b"
