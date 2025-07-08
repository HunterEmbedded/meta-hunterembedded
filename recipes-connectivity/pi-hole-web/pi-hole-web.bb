SUMMARY = "Network-wide ad blocking via your own Linux hardware"
HOMEPAGE = "https://docs.pi-hole.net/"
LICENSE = "EUPL-1.2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e0fbee952ed27d972a79a2a0e23427de"

require ../pi-hole/pi-hole.inc

RDEPENDS:${PN} = "perl"

SRC_URI = "  gitsm://github.com/pi-hole/web.git;protocol=https;branch=master"


SRCREV = "${WEB_SRCREV}"

S = "${WORKDIR}/git"



do_install(){

    # copy over pihole repo
    install -d ${D}/var/www/html/admin
    cp -r ${S}/. ${D}/var/www/html/admin/
}


FILES:${PN} += " \
    /var/www/html/admin \
"