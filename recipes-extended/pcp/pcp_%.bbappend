FILESEXTRAPATHS:append := "${THISDIR}:${THISDIR}/files:"

# Disable x11 dependency
DEPENDS:remove = "libx11"
REQUIRED_DISTRO_FEATURES:remove = "x11"

# Add pmie config for cpu usage monitor
SRC_URI += "\
    file://0001-setup-pmie-monitor-for-cpu-usage.patch \
    file://cpu_usage_monitor \
"

# PCP oss bb regression, which will overwritten USERADD_PACKAGES
USERADD_PACKAGES += "${PN}"

# Add format definition for pid file generation
do_configure:prepend () {
    sed -i '$ a printf_fmt_pid=jd' ${WORKDIR}/config.linux
}

do_install:append () {
    # Remove unneccessary bashisms script
    rm -rf ${D}${libexecdir}/pcp/bin/pmlogger_daily_report
    # Add PMIE configure file to monitor CPU usage
    install -m 0644 ${WORKDIR}/cpu_usage_monitor ${D}${localstatedir}/lib/pcp/config/pmie/cpu_usage_monitor
}

# Remove unneccessary services for only cpu usage monitor
SYSTEMD_SERVICE:${PN}:remove = "pmlogger_daily.service pmlogger_farm_check.service pmfind.service \
                                pmie_daily.service  pmlogger.service pmlogger_daily_report.service \
                                pmproxy.service pmie_farm.service pmlogger_check.service \
                                pmie_check.service pmie_farm_check.service"
