IMAGE_INSTALL += "\
    kernel-modules \
    system-core-adbd \
    system-core-leprop \
    system-core-usb \
    system-prop \
"

EXTRA_IMAGECMD:ext4 = "-i 4096 -b 4096"

