# Do not enable systemd-bootchart.service by default, but still install it onto the target
SYSTEMD_SERVICE:${PN} = ""
FILES:${PN} += "${systemd_system_unitdir}/systemd-bootchart.service"
