FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

KCONF_AUDIT_LEVEL="1"

SRC_URI:append = " \
 file://remove-vc4graphics.cfg \
 file://remove-unnecessary-drivers.cfg \
 "