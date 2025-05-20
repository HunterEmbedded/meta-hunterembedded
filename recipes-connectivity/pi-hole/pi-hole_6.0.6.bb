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

PI_HOLE_INSTALL_DIR = "/opt/pihole"
PI_HOLE_BIN_DIR = "/usr/local/bin"
inherit useradd
USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "--system pihole"

do_install(){

    # copy over pihole repo
    install -d ${D}/etc/.pihole
    cp -r ${S}/. ${D}/etc/.pihole

    # overwrite default script with customised no check and no download version
    install -m 755 ${WORKDIR}/basic-install.sh "${D}/etc/.pihole/automated install/"

    # create directories
    install -d ${D}/etc/pihole
 
    # This is installScripts() from the basic_install.sh 
    # Install the scripts by:
    #  -o setting the owner to the user
    #  -Dm755 create all leading components of destination except the last, then copy the source to the destination and setting the permissions to 755
    #
    # The first ones are the directories
    install -o pihole -Dm755 -d ${D}${PI_HOLE_INSTALL_DIR}
    install -o pihole -Dm755 -d ${D}${PI_HOLE_BIN_DIR}
    # The rest are the scripts Pi-hole needs
    install -o pihole -Dm755 ${S}/gravity.sh ${D}${PI_HOLE_INSTALL_DIR}/gravity.sh
    install -o pihole -Dm755 ${S}/advanced/Scripts/*.sh ${D}${PI_HOLE_INSTALL_DIR}/ 
    install -o pihole -Dm755 "${S}/automated install/uninstall.sh" ${D}${PI_HOLE_INSTALL_DIR}/
    install -o pihole -Dm755 ${S}/advanced/Scripts/COL_TABLE ${D}${PI_HOLE_INSTALL_DIR}/
    install -o pihole -Dm755 ${S}/pihole ${D}${PI_HOLE_BIN_DIR}/
    install -Dm644 ${S}/advanced/bash-completion/pihole ${D}/etc/bash_completion.d/pihole

 
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
    ${PI_HOLE_INSTALL_DIR} \
    ${PI_HOLE_BIN_DIR} \
"