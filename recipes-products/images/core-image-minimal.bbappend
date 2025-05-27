IMAGE_INSTALL += "kernel-modules"

EXTRA_IMAGECMD:ext4 = "-i 4096 -b 4096"

IMAGE_FEATURES:append = " ${@bb.utils.contains('VARIANT', 'debug', 'debug-tweaks ssh-server-openssh', '', d)}"

