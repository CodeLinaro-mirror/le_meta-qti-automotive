SUMMARY = "AWE Manager"
DESCRIPTION = "This is a library which provides a generic control API to AWE Controller allowing the management of the underlying AWE system."
HOMEPAGE = "https://git.codelinaro.org/clo/yocto-mirrors/awe-manager"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS += "awe-packetclient glib-2.0 awe-audiolite-osal awe-alsa awe-utils git-lfs-native"

# Tag 1.0.1 + Qualcomm patch revision 1
PV = "1.0.1+qc1"
# when you change PV also check do_configure:prepend!

SRC_URI = "git://git.codelinaro.org/clo/yocto-mirrors/awe-manager.git;protocol=https;branch=dspconcepts/main;lfs=0 \
           file://0001-awemgr-map-rc_fail_comm-errors-to-comm_timeout.patch \
           "

# Keep git-lfs-native even with lfs=0: lfs=0 skips LFS downloads, but checkout
# still needs git-lfs filter-process because the mirror keeps filter=lfs entries.
do_unpack[depends] += "git-lfs-native:do_populate_sysroot"

SRCREV = "5faef3ff1e77f4f477ad6beaa65c691b84510c3a"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

EXTRA_OECMAKE += "\
    -DAWEMGR_BUILD_SHARED_LIB=ON \
    -DAWEMGR_BUILD_USE_GLIB=ON \
    -DAWEMGR_LOGGING=AWEQ \
    -DAWEMGR_AWECORE_CONNECTION=CSHMEM \
    -DAWEMGR_BUILD_TESTS=OFF \
    -DAWE_AWC_BUILD_EXAMPLES=OFF \
    -DAWE_OSAL_BUILD_EXAMPLES=OFF \
    -DAWE_OSAL_BUILD_TESTS=OFF \
"

CFLAGS += "-DLINUX"
CFLAGS:append:gen5 = " -DPLATFORM_NORDAU -DENABLE_QLF"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

RDEPENDS:${PN} += "awe-packetclient awe-audiolite-osal awe-alsa awe-utils glib-2.0"

# Prevent cmake/GenerateVersionHeader.cmake from running 'git describe --dirty --tags'
# on the quilt-patched work tree. Writing a VERSION file takes priority over git.
do_configure:prepend() {
        echo "AWEMGR_VERSION=${PV}" > ${S}/VERSION
}
