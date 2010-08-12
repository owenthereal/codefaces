#!/bin/bash

#####################################################################
# This set up script is used to set up a web slave tomcat server for
# codefaces.org based on Ubuntu 10
#
# This script only open an AJP port for load balancer, a SSH port and 
# a ICMP ping port
#
# This include:
#   1. perform dist-upgrade
#   2. install sun-java
#   3. perform time sync
#   4. create a non-root sudoer
#   5. install and config tomcat
#   6. secure kernel using sysctl
#   6. secure SSH
#   7. secure iptables
#
# After finished the tasks. Please update the iptables using
# iptables-restore
#
# KK Lo, July 4, 2010
#

ADMIN_USER=admin
LOAD_BALANCER_IP=173.203.92.105
SSH_PORT=42597 
TOMCAT_HOME=/var/lib/tomcat6
AJP_PORT=47330
JVMROUTE=tomcat2

#####################################################################
# Overriding bashrc for environmental variables
# 
echo "Replacing .bashrc"
yes | cp templates/bashrc /root/.bashrc
source /root/.bashrc

#####################################################################
# Overriding the apt-get source file
#
echo "Replacing apt/sources.list"
yes | cp templates/sources.list /etc/apt/sources.list

#####################################################################
# Perform a system update
#
echo "Perform system update"
apt-get update
yes | apt-get dist-upgrade

#####################################################################
# Install sun-java
#
echo "Install sun-java"
yes | apt-get install sun-java6-jdk

#####################################################################
# Install tomcat
#
echo "Install Tomcat"
yes | apt-get install tomcat6
/etc/init.d/tomcat6 stop

#####################################################################
# Configure tomcat
#
echo "Configure Tomcat"
sed -e 's/\$AJP_PORT/'$AJP_PORT'/g' \
    -e 's/\$JVMROUTE/'$JVMROUTE'/g' \
    templates/server.xml > $TOMCAT_HOME/conf/server.xml
yes | rm -rf $TOMCAT_HOME/webapps/*
mkdir -p $TOMCAT_HOME/backup
yes | cp templates/web.xml $TOMCAT_HOME/conf/web.xml

#####################################################################
# Perform time sync
#
echo "Perform time sync"
ntpdate ntp.ubuntu.com
yes | cp templates/ntpdate /etc/cron.daily/ntpdate
chmod 755 /etc/cron.daily/ntpdate

#####################################################################
# Securing Kernel
#
echo "Replacing sysctl.conf"
yes | cp templates/sysctl.conf /etc/sysctl.conf
sysctl -p

#####################################################################
# Create non-root admin
#
echo "Create Non-root admin"
yes | cp templates/sudoers /etc/sudoers
chmod 440 /etc/sudoers
adduser admin
adduser admin sudo

#####################################################################
# Securing SSH
#
echo "Securing SSH"
sed -e 's/\$SSH_PORT/'$SSH_PORT'/g' \
    templates/sshd_config > /etc/ssh/sshd_config

#####################################################################
# Setting iptables
#
echo "Setting iptables"
mkdir /etc/network/iptables
sed -e 's/\$SSH_PORT/'$SSH_PORT'/g' \
    -e 's/\$AJP_PORT/'$AJP_PORT'/g' \
    -e 's/\$LOAD_BALANCER_IP/'$LOAD_BALANCER_IP'/g' \
    templates/iptables.rule > /etc/network/iptables/iptables.rule
chmod 640 /etc/network/iptables/iptables.rule
yes | cp templates/iptables_load /etc/network/if-pre-up.d/iptables_load

#####################################################################
# Restart SSH
#
echo "Restart SSH"
echo 'The new SSH PORT is '$SSH_PORT ' with user '$ADMIN_USER
echo "and login to run iptables-restore < /etc/network/iptables/iptables.rule"
/etc/init.d/ssh restart
