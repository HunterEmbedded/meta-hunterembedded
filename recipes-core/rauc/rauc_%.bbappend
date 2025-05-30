do_install:append() {

   # Image is only marked as good once pi-hole.service has completed
   sed -i "s/After=boot-complete.target/After=pi-hole.service/" ${D}${systemd_unitdir}/system/rauc-mark-good.service
   sed -i "s/Requires=boot-complete.target/Requires=pi-hole.service/" ${D}${systemd_unitdir}/system/rauc-mark-good.service
}

