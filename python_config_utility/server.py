#!/usr/bin/python

from os import system
from socket import *

FALSE_PORT = 1337
TRUE_PORT = 1338

stp = True


system("cp /etc/config/stp /etc/config/network")
system("/etc/init.d/network restart")

while True:
    if stp:
        sockTrue = socket(AF_INET, SOCK_DGRAM)
        sockTrue.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
        sockTrue.bind(('', TRUE_PORT))
        data, addr = sockTrue.recvfrom(1024)
        system("cp /etc/config/noStp /etc/config/network")
    else:
        sockFalse = socket(AF_INET, SOCK_DGRAM)
        sockFalse.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
        sockFalse.bind(('', FALSE_PORT))
        data, addr = sockFalse.recvfrom(1024)
        system("cp /etc/config/stp /etc/config/network")

    system("/etc/init.d/network restart")
    stp = not stp
