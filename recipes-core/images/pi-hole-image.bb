require recipes-core/images/core-image-minimal.bb

DESCRIPTION = "Optimized image for Pi-Hole"

require pi-hole-packages.inc


inherit extrausers

IMAGE_INSTALL:append = " sudo"

# This password is generated with `openssl passwd -6 pihole` with the $ character escaped
PASSWD = "\$6\$K4WZJ7vtv0TmkelK\$s05QnWh6Z4G43PP/Eb4K4fmQKwlNuEyyvYOS7CQsQIAq6zSWQ9XjBdHPnN48r/zaJvRRphflWwJVLBrQTkwSv/"
EXTRA_USERS_PARAMS = "\
    useradd  -d /home/pihole --groups sudo --shell /bin/sh --password '\$6\$K4WZJ7vtv0TmkelK\$s05QnWh6Z4G43PP/Eb4K4fmQKwlNuEyyvYOS7CQsQIAq6zSWQ9XjBdHPnN48r/zaJvRRphflWwJVLBrQTkwSv/' pihole; \
"

# Store the kernel in the rootfs partition
IMAGE_INSTALL:append = " kernel-image kernel-modules"

# Remove the kernel from the /boot partition because it is in rootfs
RPI_EXTRA_IMAGE_BOOT_FILES:remove = "${KERNEL_IMAGETYPE}"
