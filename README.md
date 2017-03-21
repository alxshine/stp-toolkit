#STP demonstration toolkit

Connecting to the routers
=======
The routers all share the same address, 192.168.1.1, and are in a 24 bit subnet (subnet mask 255.255.255.0).
They are somewhat reluctant with answering ARP requests, which makes connecting to them a bit of a pain.
I have found that launching an nmap scan of the subnet (no port scans required, use -sn to save time) can circumvent this.

Launching the python script
=======
The python scripts for controlling the STP behaviour of the routers are located in the python_config_util subfolder.
They should all be executable, and you should be able to start them from the command line.
To disable/enable STP on the routers, run the client.py script with on/off as parameter.
The script should now have a more verbose message when the parameter is missing.
After the client.py script was run, please wait for the network interfaces of the router to come up again.
It takes a few seconds after the lights have come up again for the socket on the server to be recreated.
Running the client script again before the server is done recreating the socket may cause undefined behaviour.
