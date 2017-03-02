#!/usr/bin/python

import socket
from subprocess import call

FALSE_PORT = 1337
TRUE_PORT = 1338

stp = False
sockFalse = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sockFalse.bind(('', FALSE_PORT))

sockTrue = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sockTrue.bind(('', TRUE_PORT))

call(["cp", "/etc/config/noStp", "/etc/config/network"])
call(["/etc/init.d/network", "restart"])

while True:
    if stp:
        data, addr = sockTrue.recvfrom(1024)
        call(["cp", "/etc/config/noStp", "/etc/config/network"])
    else:
        data, addr = sockFalse.recvfrom(1024)
        call(["cp", "/etc/config/stp", "/etc/config/network"])

    call(["/etc/init.d/network", "restart"])
    stp = not stp
