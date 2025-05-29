SUMMARY = "Network-wide ad blocking via your own Linux hardware"
HOMEPAGE = "https://docs.pi-hole.net/"
LICENSE = "EUPL-1.2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ad78970d0f0174fa07b68411168b2378"

require ../pi-hole/pi-hole.inc

DEPENDS = " \
    gmp \
    libidn2 \
    libunistring \
    mbedtls \
    nettle \
    readline \
    xxd-native \
"

SRC_URI = " \
    gitsm://github.com/pi-hole/ftl.git;protocol=https;branch=master \
    file://0001-correct-type-for-tx-offset-printf.patch \
    file://90-pi-hole-disable-stub-listener.conf \
"
SRCREV = "${FTL_SRCREV}"

S = "${WORKDIR}/git"

inherit cmake pkgconfig


OECMAKE_GENERATOR = "Unix Makefiles"

do_install:append() {

    install -d ${D}${sysconfdir}/systemd/resolved.conf.d
    install -Dm644  ${WORKDIR}/90-pi-hole-disable-stub-listener.conf ${D}${sysconfdir}/systemd/resolved.conf.d/
}

FILES:${PN} += " \
    ${sysconfdir}/systemd/resolved.conf.d \
    "
