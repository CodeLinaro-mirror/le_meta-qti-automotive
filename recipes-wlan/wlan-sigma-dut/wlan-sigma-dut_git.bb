SUMMARY = "WFA Certification Test Tool"
DESCRIPTION = "Sigma_dut is a application that used for WFA certification testing. \
               The WFA has developed an interoperability test suite. Working in \
               conjunction with an authorized test lab, these tests are performed \
               on vendor products. And the tool runing on the vendor products to \
               pass WFA certification case is sigma_dut this tool. And this tool \
               can support all kinds of test case, for example WMM-PS \
               (WMM-Powersave_testplan_v2-1-6.pdf), TGn (TGnInteropTP_2.8.pdf), \
               WPS (Wi-Fi_Protected_Setup_Test_Plan_v2.0.15.pdf), \
               Wi-Fi Direct (wfa_wifi_direct_interoperability_test_plan_version_1.3.pdf) \
               and so on.\
              "
HOMEPAGE = "https://github.com/qca/sigma-dut"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://README;md5=edb3527809487b74b4d4a7e02b05acf0"

DEPENDS += "libnl"

SRC_URI = "${PATH_TO_REPO}/wlan/utils/sigma-dut/.git;protocol=${PROTO};destsuffix=wlan/utils/sigma-dut;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/wlan/utils/sigma-dut"

inherit autotools-brokensep pkgconfig

CFLAGS += "-I${STAGING_INCDIR}/libnl3/"
EXTRA_OEMAKE += "NL80211_SUPPORT=y"

do_install() {
    make install DESTDIR=${D} BINDIR=${sbindir}/
}

PACKAGE_ARCH ?= "${MACHINE_ARCH}"
