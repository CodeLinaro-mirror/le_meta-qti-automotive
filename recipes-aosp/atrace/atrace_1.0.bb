SUMMARY = "atrace utility for tracing"
DESCRIPTION = "atrace utility for colloect tracing on board"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0 & BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"
LIC_FILES_CHKSUM += "file://${COMMON_LICENSE_DIR}/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "libbase libcutils libutils zlib liblog"

SRC_URI = "${CLO_LA_GIT}/platform/frameworks/native;name=native;protocol=https;branch=aosp-new/android11-gsi;destsuffix=git \
           file://atrace_le.patch \
          "
# matches with android11-mainline-release release
SRCREV = "b9adcf3bf476d7b2b529a7c21a402590480d4fdd"
S = "${WORKDIR}/git"

LDFLAGS += "-lpthread -lbase -llog -lutils -lcutils -lz"
do_compile() {
 ${CXX} ${S}/cmds/atrace/atrace.cpp ${CFLAGS} ${CPPFLAGS} ${LDFLAGS} -o atrace
}

do_install() {
     install -d ${D}${bindir}
     install -m 0755 atrace ${D}${bindir}
}
