import socket
import sys

UDP_TARGET = "localhost"
MESSAGE = "AOEU"

port = int(sys.argv[1])

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.sendto(MESSAGE, (UDP_TARGET,port))
