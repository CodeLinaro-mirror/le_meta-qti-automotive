# To use this class, inherit the class AFTER inheriting systemd class.


# Purpose of this bitbake class:
#
# Hacky solution to get systemd.bbclass's systemd_check_services function to play nice
# when a package enables an instance of a systemd service that uses a systemd service
# template installed by a different package.
#
# Without this solution, systemd_check_services causes the do_package to fail because it can not
# find the systemd base service file sleep-notify@.service


# How this class works to fix the problem:
#
# The upstream systemd.bbclass runs systemd_check_services as a PACKAGESPLITFUNCS function during
# the bitbake do_package task. https://git.yoctoproject.org/poky/tree/meta/classes/systemd.bbclass?h=kirkstone-4.0.22#n145
#
# When using SYSTEMD_SERVICES:${PN}="sleep-notify@abc.service", systemd_check_services()
# requires that the base service file sleep-notify@.service is present in ${D}, meaning the current package
# had installed the file sleep-notify@.service.
#
# However, in our use case, power-utils package installs sleep-notify@.service, and other packages
# enable custom instances of the service, for example sleep-notify@abc.service or sleep-notify@xyz.service
#
# The hack to fix this is to, before systemd_check_services() runs, copy sleep-notify@.service from
# the recipe sysroot to the destination path ${D}. Then after systemd_check_services() runs, remove
# sleep-notify@.service from the destination path.
#
# To ensure proper ordering of add_sleep_notify_template_hack(), systemd_check_services(), then
# rm_sleep_notify_template_hack(), this sleep-notify-service bbclass must be inherited after the
# systemd bbclass is inherited.

DEPENDS:append:class-target = " power-utils"

python __anonymous() {
    # ensure packaging starts before sysroot gets populated, so sleep-notifiy@.service will not be included in the sysroot.
    # It is to avoid the following error:
    #     do_prepare_recipe_sysroot: The file /usr/lib/systemd/system/sleep-notify@.service is installed by both power-utils and xxxx, aborting
    d.appendVarFlag('do_populate_sysroot', 'depends', " %s:do_package" % d.getVar('PN', True))
}

add_sleep_notify_template_hack() {
    cp ${RECIPE_SYSROOT}/${systemd_system_unitdir}/sleep-notify@.service ${D}${systemd_system_unitdir}/sleep-notify@.service
}

rm_sleep_notify_template_hack() {
    rm ${D}${systemd_system_unitdir}/sleep-notify@.service
}




PACKAGESPLITFUNCS:prepend = " add_sleep_notify_template_hack "
PACKAGESPLITFUNCS:append = " rm_sleep_notify_template_hack "
