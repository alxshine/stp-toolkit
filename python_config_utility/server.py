import socket

FALSE_PORT = 1337
TRUE_PORT = 1338

stp = False
sockFalse = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sockFalse.bind(('', FALSE_PORT))

sockTrue = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sockTrue.bind(('', TRUE_PORT))

while True:
    if stp:
        data, addr = sockTrue.recvfrom(1024)
        print("deactivating STP")
    else:
        data, addr = sockFalse.recvfrom(1024)
        print("activating STP")

    stp = not stp
