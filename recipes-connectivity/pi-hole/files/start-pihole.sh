#!/usr/bin/env bash
# script to be run which will configure pihole for RAUC A/B with shared data if required

# if /data/etc/pihole does not exist then it is first boot from a WIC file so configure system
if [ ! -d /data/etc/pihole ]
then
    /opt/pihole/move-pihole-config-to-data.sh
    /opt/pihole/basic-install.sh     

    # for some unknown (as yet) reason need to manually force pihole to understand that it has downloaded gravity db
    pihole -g

    # create /data/update owned by admin to receive the update rauc bundles by scp
    mkdir /data/update
    chown admin:admin /data/update

# else if it does exist but /etc/pihole is not a symlink then it is first boot of RAUC image and so symlinks need created
elif [ ! -L /etc/pihole ]
then

    # a special case is /etc/pihole/versions as we always want to update /data version from rootfs as it is tied to 
    # rootfs contents rather than being a persistent file to be maintained 
    mv /etc/pihole/versions /data/etc/pihole/versions

    /opt/pihole/use-pihole-config-from-data.sh
fi

