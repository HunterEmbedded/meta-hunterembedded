DESCRIPTION = "RAUC bundle generator"

inherit bundle

require ../images/pi-hole-image-version.inc

RAUC_BUNDLE_COMPATIBLE = "${MACHINE}"
RAUC_BUNDLE_VERSION = "${PIHOLE_IMAGE_VERSION}"
RAUC_BUNDLE_DESCRIPTION = "RAUC PiHole Bundle"

RAUC_BUNDLE_FORMAT = "verity"

RAUC_BUNDLE_SLOTS = "rootfs"
RAUC_SLOT_rootfs = "pi-hole-image"
RAUC_SLOT_rootfs[fstype] = "ext4"

# update bundle name to include the pihole version number
BUNDLE_BASENAME:append = "-${IMAGE_PIHOLE_VERSION}"
# can't do the simple append on IMAGE_NAME as it is used to pull in recipes.
# So manipulate the next variable used to build file name with a prepend to get same effect
IMAGE_MACHINE_SUFFIX:prepend = "-${IMAGE_PIHOLE_VERSION}"

RAUC_KEY_FILE ?= "${THISDIR}/files/development-1.key.pem"
RAUC_CERT_FILE ?= "${THISDIR}/files/development-1.cert.pem"
