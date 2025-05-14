SUMMARY = "Network-wide ad blocking via your own Linux hardware"
HOMEPAGE = "https://docs.pi-hole.net/"
LICENSE = "EUPL-1.2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b88cc6a18c38fa0e92cb1bb7f97b4f8f"

require ../pi-hole/pi-hole.inc

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


SRCREV = "${CORE_SRCREV}"

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


    # create revisions file
    echo "\
CORE_VERSION=${BB_CORE_VERSION} 
CORE_BRANCH=${BB_CORE_BRANCH} 
CORE_HASH=${BB_CORE_HASH}
GITHUB_CORE_VERSION=${BB_CORE_VERSION}
GITHUB_CORE_HASH=${BB_CORE_HASH}
WEB_VERSION=${BB_WEB_VERSION} 
WEB_BRANCH=${BB_WEB_BRANCH} 
WEB_HASH=${BB_WEB_HASH}
GITHUB_WEB_VERSION=${BB_WEB_VERSION}
GITHUB_WEB_HASH=${BB_WEB_HASH}
FTL_VERSION=${BB_FTL_VERSION} 
FTL_SRCREV_BRANCH=${BB_FTL_BRANCH} 
FTL_HASH=${BB_FTL_HASH}
GITHUB_FTL_VERSION=${BB_FTL_VERSION}
GITHUB_FTL_HASH=${BB_FTL_HASH}
" > ${D}/etc/pihole/versions

}


FILES:${PN} += " \
    /etc/pihole \
    /opt/pihole \
"