#!/usr/bin/env bash
# script to be run on first boot on a RAUC bundle update to create
# symlinks from /etc to the pre-existing /etc/data 

source /opt/pihole/file-dirs-to-migrate.sh

linkFileToData () {
    # file is arg 1, it may be a path to a file or a directory in this function
    file="$1"
    echo "file $file"
    # get path and filename from file
    # path will be used to create directories on /data to hold the files
    name="${file##*/}"
    path="${file:0:${#file} - ${#name}}"


    # if file exists but is not a symlink then remove it and create symlink to the /data version
    if [ -f "$file" ] ; then
        rm $1 || exit
        ln -s  $dataDir$file $file || exit
    fi

    # if dir exists but is not a symlink then remove it and create symlink to the /data version
    if [ -d "$file" ] ; then
        rm $file || exit
        ln -s $dataDir$file $file || exit
    fi
}

linkToDirInData () {
    # dir is arg 1
    dir="$1"
    echo "dir $dir"

    rmdir $dir || exit
    ln -s $dataDir$dir $dir || exit
}

for f in $listOfFilesDirsToMove;
do 
    linkFileToData $f
done

for d in $listOfDirsToCreate
do
    linkToDirInData $d || exit 
done