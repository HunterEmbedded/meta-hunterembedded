# meta-hunter-embedded

yocto layer for Hunter Embedded customisations to implement a RAUC based A/B update mechanism for Pi-Hole.
A fixed version of Pi-Hole is built and added to the image and is locked down in the sense that the normal update
mechanisms for Pi-Hole are disabled.
To maintain system behaviour on an update all configuration data is in a shared /data partition and so will be persistent between 
image updates.
