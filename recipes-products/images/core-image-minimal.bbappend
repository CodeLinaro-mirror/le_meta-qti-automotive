IMAGE_INSTALL += "kernel-modules"

EXTRA_IMAGECMD:ext4 = "-i 4096 -b 4096"

IMAGE_FEATURES:append = " ${@bb.utils.contains('VARIANT', 'debug', 'debug-tweaks ssh-server-openssh', '', d)}"

# Set up for handling the generation of the /usr image
# partition...
require recipes-products/images/automotive-usr-image.inc
