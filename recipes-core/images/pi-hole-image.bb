require recipes-core/images/core-image-minimal.bb

DESCRIPTION = "Optimized image for Pi-Hole"

require pi-hole-packages.inc
require pi-hole-image-version.inc

inherit extrausers

# Add pihole version to image name
IMAGE_BASENAME:append = "-${IMAGE_PIHOLE_VERSION}"

IMAGE_INSTALL:append = " sudo"

# This password is generated with `openssl passwd -6 pihole` with the $ character escaped
PASSWD = "\$6\$K4WZJ7vtv0TmkelK\$s05QnWh6Z4G43PP/Eb4K4fmQKwlNuEyyvYOS7CQsQIAq6zSWQ9XjBdHPnN48r/zaJvRRphflWwJVLBrQTkwSv/"
EXTRA_USERS_PARAMS = "\
    useradd  -d /home/admin --groups sudo --shell /bin/bash --password '\$6\$K4WZJ7vtv0TmkelK\$s05QnWh6Z4G43PP/Eb4K4fmQKwlNuEyyvYOS7CQsQIAq6zSWQ9XjBdHPnN48r/zaJvRRphflWwJVLBrQTkwSv/' admin; \
"

# Store the kernel in the rootfs partition
IMAGE_INSTALL:append = " kernel-image kernel-modules"

# Remove the kernel from the /boot partition because it is in rootfs
RPI_EXTRA_IMAGE_BOOT_FILES:remove = "${KERNEL_IMAGETYPE}"
# and add uboot.env to /boot so it persists between A/B boots
#RPI_EXTRA_IMAGE_BOOT_FILES:append = " u-boot-initial-env"

# add a new CONVERSION operation to copy the .wic.bz2 file to a .img file to be friendly to 
# Raspberry Pi Imager which expects a .img
# CONVERSION operation is automatically appended to do_image_wic()
CONVERSIONTYPES:append = " img"
IMAGE_FSTYPES:append = " wic.bz2.img"
# use dd and its conv=sync option to pad the img file to a multiple of sector size to keep Imager happy
CONVERSION_CMD:img = "dd if=${IMAGE_NAME}.wic.bz2 of=${IMAGE_NAME}.img conv=sync"

