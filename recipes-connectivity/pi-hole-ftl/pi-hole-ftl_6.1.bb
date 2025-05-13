SUMMARY = "Network-wide ad blocking via your own Linux hardware"
HOMEPAGE = "https://docs.pi-hole.net/"
LICENSE = "EUPL-1.2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ad78970d0f0174fa07b68411168b2378"

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
"
SRCREV = "a3313229c21eefcb608e5d30b7255723d9efa3a9"

S = "${WORKDIR}/git"

inherit cmake pkgconfig


OECMAKE_GENERATOR = "Unix Makefiles"

#EXTRA_OECMAKE = " --trace-expand"

#do_install:append() {
#    install -d ${D}/var/log/pihole
#}

#FILES:${PN} += " /var/log/pihole"

