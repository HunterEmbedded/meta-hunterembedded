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
SRC_URI:append = " file://basic-install.sh \
                   file://file-dirs-to-migrate.sh \
                   file://move-pihole-config-to-data.sh \
                   file://use-pihole-config-from-data.sh \
                   file://pihole-default-${PV}.toml\ 
                   file://start-pihole.sh \
                   file://pi-hole.service \
                   file://pihole-FTL.service \
                  "


SRCREV = "${CORE_SRCREV}"

S = "${WORKDIR}/git"

PI_HOLE_INSTALL_DIR = "/opt/pihole"
PI_HOLE_CONFIG_DIR = "${sysconfdir}/pihole"
PI_HOLE_BIN_DIR = "/usr/local/bin"

inherit useradd systemd

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "--system pihole"

SYSTEMD_SERVICE:${PN} = "pi-hole.service"

do_install(){

    # overwrite default basic-install script with customised no check and no download version
    # as well as the supporting scripts to be run by the service
    install -d ${D}/opt/pihole
    install -m 755 ${WORKDIR}/basic-install.sh ${D}/opt/pihole
    install -m 755 ${WORKDIR}/file-dirs-to-migrate.sh ${D}/opt/pihole
    install -m 755 ${WORKDIR}/move-pihole-config-to-data.sh ${D}/opt/pihole
    install -m 755 ${WORKDIR}/use-pihole-config-from-data.sh ${D}/opt/pihole
    install -m 755 ${WORKDIR}/start-pihole.sh ${D}/opt/pihole

    # create directories
    install -o pihole -d ${D}${sysconfdir}/pihole
    install -d ${D}${sysconfdir}/pihole/Templates
    install -d ${D}${sysconfdir}/cron.d
    install -d ${D}${sysconfdir}/systemd/system/multi-user.target.wants
 
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
    install -Dm644 ${S}/advanced/bash-completion/pihole ${D}${sysconfdir}/bash_completion.d/pihole

    # This is installScripts() from basic_install.sh
    install -o pihole -g pihole -Dm660 /dev/null ${D}/${PI_HOLE_CONFIG_DIR}/hosts/custom.list
    # install pihole-FTL.service and then manually enable it with symlink as we cannot do it in the pihole-FTL recipe 
    # as it does not have the pihole git repo with the service file
    install -Dm644 ${S}/advanced/Templates/pihole-FTL.systemd ${D}${sysconfdir}/systemd/system/pihole-FTL.service
    #install -Dm644 ${WORKDIR}/pihole-FTL.service ${D}${sysconfdir}/systemd/system/pihole-FTL.service
    ln -s -r ${D}${sysconfdir}/systemd/system/pihole-FTL.service ${D}${sysconfdir}/systemd/system/multi-user.target.wants/pihole-FTL.service 
    install -Dm755 ${S}/advanced/Templates/pihole-FTL-prestart.sh ${D}${PI_HOLE_INSTALL_DIR}/pihole-FTL-prestart.sh
    install -Dm755 ${S}/advanced/Templates/pihole-FTL-poststop.sh ${D}${PI_HOLE_INSTALL_DIR}/pihole-FTL-poststop.sh

    # Install control files for cron
    install -o root -g root -Dm644  ${S}/advanced/Templates/pihole.cron ${D}${sysconfdir}/cron.d/pihole
    # and logRotate
    install -o root -g root -Dm644  ${S}/advanced/Templates/logrotate ${D}${sysconfdir}/pihole/logrotate


    # Copy gravity db files to /etc/pihole and then update script to point to that directory
    cp -r ${S}/advanced/Scripts/database_migration ${D}${sysconfdir}/pihole
    cp -r ${S}/advanced/Templates/gravity*.sql ${D}${sysconfdir}/pihole/Templates

    sed -i "s?/etc/.pihole/advanced/Scripts?/etc/pihole?" ${D}${PI_HOLE_INSTALL_DIR}/gravity.sh
    sed -i "s?piholeGitDir=\"/etc/.pihole\"??" ${D}${PI_HOLE_INSTALL_DIR}/gravity.sh
    # pick up two instances of string with g option
    sed -i "s?\${piholeGitDir}/advanced/Templates?/etc/pihole/Templates?g"  ${D}${PI_HOLE_INSTALL_DIR}/gravity.sh

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


    # install a pre-configured pihole.toml with all the default settings for DNS etc already set. This avoids all
    # the dialog boxes being shown. UI password set to "piholeUI" using "pihole setpassword piholeUI" on target to 
    # create value in pihole.toml
    install -o pihole -Dm644 ${WORKDIR}/pihole-default-${PV}.toml  ${D}${PI_HOLE_CONFIG_DIR}/pihole.toml

    # remove updatechecker from cron job as it changes /etc/pihole/versions and thus UI dashboard
    # we are on fixed versions and so do not want an automatic update check
    sed -i "s/59 17/#59 17/" ${D}${sysconfdir}/cron.d/pihole
    sed -i "s/@reboot root/#@reboot root/" ${D}${sysconfdir}/cron.d/pihole

    if [ "${@bb.utils.filter('DISTRO_FEATURES', 'systemd', d)}" ] ; then
        install -d ${D}${systemd_system_unitdir}
        install -m 0644 ${WORKDIR}/pi-hole.service ${D}${systemd_system_unitdir}/pi-hole.service
    fi
}


FILES:${PN} += " \
    /opt/pihole \
    /etc/pihole \
    ${PI_HOLE_INSTALL_DIR} \
    ${PI_HOLE_CONFIG_DIR} \
    ${PI_HOLE_BIN_DIR} \
    ${systemd_system_unitdir} \
"
