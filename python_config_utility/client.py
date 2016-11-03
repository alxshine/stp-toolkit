import socket

UDP_TARGET = "localhost"
UDP_PORT = 1337
MESSAGE = "AOEU"

print("sending: " + MESSAGE + " to " + UDP_TARGET)

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.sendto(MESSAGE, (UDP_TARGET,UDP_PORT))
