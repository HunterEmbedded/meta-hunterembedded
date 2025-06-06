#!/usr/bin/env bash
# script to be run which will configure pihole for RAUC A/B with shared data if required

# if /data/etc/pihole does not exist then it is first boot from a WIC file so configure system
if [ ! -d /data/etc/pihole ]
then
    /opt/pihole/move-pihole-config-to-data.sh
    /opt/pihole/basic-install.sh     

    # for some unknown (as yet) reason need to manually force pihole to understand that it has downloaded gravity db
    pihole -g
# else if it does exits but /etc/pihole is not a symlink then it is first boot of RAUC image and so symlinks need created
elif [ ! -L /etc/pihole ]
then
    /opt/pihole/use-pihole-config-from-data.sh
fi

