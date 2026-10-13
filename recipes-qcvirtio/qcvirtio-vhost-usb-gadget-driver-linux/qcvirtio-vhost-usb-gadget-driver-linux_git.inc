QCVIRTIO_VHOST_USB_GADGET_DRIVER_PATH = "vendor/qcom/opensource/qcvirtio-host-drivers/qcvirtio-vhost-usb-gadget-driver-linux"
SRC_URI = "\
    ${PATH_TO_REPO}/${QCVIRTIO_VHOST_USB_GADGET_DRIVER_PATH}/.git;protocol=${PROTO};destsuffix=${QCVIRTIO_VHOST_USB_GADGET_DRIVER_PATH};usehead=1 \
"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/${QCVIRTIO_VHOST_USB_GADGET_DRIVER_PATH}"

