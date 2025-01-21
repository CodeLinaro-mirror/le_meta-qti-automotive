IMAGE_INSTALL += "\
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-location', 'packagegroup-qti-location-hal', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-lxc', 'packagegroup-qti-lxc', '', d)} \
"

# Add libgomp support
IMAGE_INSTALL += "libgomp"

# Introducing selinux-image.bbclass is to label selinux contexts when build.
inherit ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'selinux-image', '', d)}

IMAGE_INSTALL += "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'packagegroup-selinux-minimal packagegroup-selinux-policycoreutils checkpolicy secilc auditd selinux-policy', '', d)} \
"
