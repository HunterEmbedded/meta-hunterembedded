# based on instructions at https://docs.pi-hole.net/ftldns/compile/

EXTRA_OECONF:remove:rpiX = "--disable-static"
EXTRA_OECONF:append:rpiX = " \
    --enable-static \
    --disable-shared \
    --disable-openssl \
    --disable-mini-gmp \
    --disable-gcov \
    --disable-documentation \
 "
