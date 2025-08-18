SUMMARY = "Real-Time Linux Analysis tools"
DESCRIPTION = "The rtla is a meta-tool that includes a set of commands that aims \
to analyze the real-time properties of Linux. But instead of testing Linux as a \
black box, rtla leverages kernel tracing capabilities to provide precise \
information about the properties and root causes of unexpected results."
HOMEPAGE = "https://www.kernel.org/doc/html/v6.1/tools/rtla/index.html"

LICENSE = "GPL-2.0-only"

DEPENDS += "\
    libtracefs \
    libtraceevent \
"

PROVIDES = "virtual/rtla"

inherit kernelsrc pkgconfig

do_configure[depends] += "virtual/kernel:do_shared_workdir"

do_populate_lic[depends] += "virtual/kernel:do_shared_workdir"

EXTRA_OEMAKE = "\
    -C ${S}/tools/tracing/rtla \
    DESTDIR="${D}" \
    CROSS_COMPILE=${TARGET_PREFIX} \
    EXTRA_LDFLAGS="-pthread" \
"

do_compile() {
    # do not compile the documentation
    oe_runmake rtla -o doc
}

do_install() {
    # do not install the documentation
    oe_runmake install -o doc_install
}

INSANE_SKIP:${PN} += "already-stripped"

TARGET_CC_ARCH += "${LDFLAGS}"
