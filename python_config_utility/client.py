from socket import *
import sys

MESSAGE = "AOEU"

port = int(sys.argv[1])

sock = socket(AF_INET, SOCK_DGRAM)
sock.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
sock.setsockopt(SOL_SOCKET, SO_BROADCAST, 1)
sock.sendto(MESSAGE, ('192.168.1.255',port))
