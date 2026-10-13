SUMMARY = "VirGL virtual OpenGL renderer"
DESCRIPTION = "Virgil is a research project to investigate the possibility of \
creating a virtual 3D GPU for use inside qemu virtual machines, that allows \
the guest operating system to use the capabilities of the host GPU to \
accelerate 3D rendering."
HOMEPAGE = "https://virgil3d.github.io/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=c81c08eeefd9418fca8f88309a76db10"

DEPENDS = "libdrm libepoxy python3-pyyaml-native virtual/egl virtual/libgbm"

VIRGLRENDERER_PATH = "external/virglrenderer"
SRC_URI = "${PATH_TO_REPO}/${VIRGLRENDERER_PATH}/.git;protocol=${PROTO};destsuffix=${VIRGLRENDERER_PATH}"

SRCREV = "${AUTOREV}"
S = "${WORKDIR}/${VIRGLRENDERER_PATH}"

inherit meson pkgconfig features_check

BBCLASSEXTEND = "native nativesdk"

REQUIRED_DISTRO_FEATURES = "opengl"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'vulkan', d)}"
PACKAGECONFIG[vulkan] = "-Dvenus=true,-Dvenus=false,vulkan-loader vulkan-headers"

EXTRA_OEMESON:append = " \
            -Dplatforms=egl \
            ${@bb.utils.contains('PACKAGECONFIG', 'vulkan', '', '-Dvenus=false', d)} \
            "
