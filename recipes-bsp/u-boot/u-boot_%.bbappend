do_install:append() {

    # update fw_env.config to point to u-boot partition (partition 1) mounted in linux as /u-boot which contains
    # all the u-boot related files - binary, dtb and env.
    # This separates the uboot files from the kernel binary which is contained in rootfs in /boot
    # uboot.env must be on a FAT partition otherwise uboot can't read it.
    sed -i "s?/boot/uboot.env?/u-boot/uboot.env?" ${D}/etc/fw_env.config
}
