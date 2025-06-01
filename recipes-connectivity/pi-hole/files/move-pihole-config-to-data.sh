#!/usr/bin/env bash
# script to be run on first boot on a clean image to move list of files in /etc that change 
# when pihole is installed from rootfs /etc to a symlink on /data 

source /opt/pihole/file-dirs-to-migrate.sh

moveToData () {
    # file is arg 1, it may be a path to a file or a directory in this function
    file="$1"
    echo "file $file"
    # get path and filename from file
    # path will be used to create directories on /data to hold the files
    name="${file##*/}"
    path="${file:0:${#file} - ${#name}}"


    # if file exists but is not a symlink then move it and create symlink
    if [ -f "$file" ] ; then
        # create new directory to hold the file
        mkdir -p $dataDir$path || exit
        mv $1 $dataDir$path$name || exit
        ln -s  $dataDir$file $file || exit
    fi

    # if dir exists but is not a symlink then move it and create symlink
    if [ -d "$file" ] ; then
         mv $file $dataDir$file || exit
        ln -s $dataDir$file $file || exit
    fi
}

createDirInData () {
    # dir is arg 1
    dir="$1"
    echo "dir $dir"

    mkdir -p $dataDir$dir || exit
    ln -s $dataDir$dir $dir || exit
}

# If /data/etc does not exist then create it
if [ ! -d "$dataDir/etc" ]; then 
    mkdir $dataDir/etc || exit 
fi


for f in $listOfFilesDirsToMove;
do 
    moveToData $f
done

for d in $listOfDirsToCreate
do
    createDirInData $d || exit 
done