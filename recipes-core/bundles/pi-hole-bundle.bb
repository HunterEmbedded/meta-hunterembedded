DESCRIPTION = "RAUC bundle generator"

inherit bundle

RAUC_BUNDLE_COMPATIBLE = "${MACHINE}"
RAUC_BUNDLE_VERSION = "v6.0.6"
RAUC_BUNDLE_DESCRIPTION = "RAUC PiHole Bundle"

RAUC_BUNDLE_FORMAT = "verity"

RAUC_BUNDLE_SLOTS = "rootfs"
RAUC_SLOT_rootfs = "pi-hole-image"
RAUC_SLOT_rootfs[fstype] = "ext4"

RAUC_KEY_FILE ?= "${THISDIR}/files/development-1.key.pem"
RAUC_CERT_FILE ?= "${THISDIR}/files/development-1.cert.pem"
