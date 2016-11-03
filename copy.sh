#!/bin/bash

scp ./upstart/udp root@${1}:/etc/init.d/
scp ./python_config_utility/server.py root@${1}:/usr/sbin/
ssh root@${1} '/etc/init.d/udp enable && reboot'
