#!/usr/bin/python

from socket import *
import sys

MESSAGE = "AOEU"

target = sys.argv[1]
if target=="on":
    port = 1337
else:
    port = 1338

sock = socket(AF_INET, SOCK_DGRAM)
sock.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
sock.setsockopt(SOL_SOCKET, SO_BROADCAST, 1)
sock.sendto(MESSAGE, ('192.168.1.255',port))
