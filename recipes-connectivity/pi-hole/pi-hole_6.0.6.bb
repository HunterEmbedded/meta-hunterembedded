SUMMARY = "Network-wide ad blocking via your own Linux hardware"
HOMEPAGE = "https://docs.pi-hole.net/"
LICENSE = "EUPL-1.2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b88cc6a18c38fa0e92cb1bb7f97b4f8f"

#DEPENDS = " \
#    pi-hole-ftl \
#    pi-hole-web \
#"

RDEPENDS:${PN} = " \
    bash-completion \
    binutils \
    ca-certificates \
    cronie \
    curl \
    dialog  \
    git \
    grep \
    iproute2 \
    iproute2-ss \
    iputils-ping \
    jq \
    libcap \
    libcap-bin \
    lshw \
    netcat-openbsd \
    perl \
    pi-hole-ftl \
    pi-hole-web \
    procps \
    psmisc \
    sudo \
    unzip \
"

SRC_URI = "  gitsm://github.com/pi-hole/pi-hole.git;protocol=https;branch=master"
SRC_URI:append = " file://basic-install.sh"


SRCREV = "0f7803b7753b581ed747eb6398be0c78dbfdc845"

S = "${WORKDIR}/git"



do_install(){

    # copy over pihole repo
    install -d ${D}/etc/.pihole
    cp -r ${S}/. ${D}/etc/.pihole

    # overwrite default script with customised no check and no download version
    install -m 755 ${WORKDIR}/basic-install.sh "${D}/etc/.pihole/automated install/"

    # create directories
    install -d ${D}/etc/pihole
    install -d ${D}/opt/pihole

}


FILES:${PN} += " \
    /etc/pihole \
    /opt/pihole \
"