# Fix error: nothing provides /bin/sed needed by kernel-devsrc-1.0-r0
# scripts/mksysmap depends on sed
RDEPENDS:${PN}:append = " sed"
