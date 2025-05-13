do_install:append() {
    sed -i  "s/# %sudo	ALL=(ALL:ALL) ALL/%sudo	ALL=(ALL:ALL) ALL/" ${D}${sysconfdir}/sudoers
}


