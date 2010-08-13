#!/bin/bash

#####################################################################
# This set up script is used to set up a web master server for
# codefaces.org based on Ubuntu 10.
#
# This script MUST be run after the slave server setup script
#
# This include:
#   1. install apache2 and modjk
#   2. install cronolog
#   3. get apache ready
#   4. secure iptables
#
# KK Lo, August 12, 2010
#

SSH_PORT=42597

#####################################################################
# Install Apache2
#
echo "Installing Apache2"
yes | apt-get install apache2

#####################################################################
# Install modjk
#
echo "Installing modjk"
yes | apt-get install libapache2-mod-jk

#####################################################################
# Setting up modjk
#
echo "Setting modjk"
yes | cp templates/workers.properties /etc/apache2/

#####################################################################
# Install Cronolog
#
echo "Installing Cronolog"
yes | apt-get install cronolog

#####################################################################
# Setting Cronolog log folder for Apache2
#
echo "Setting Cronolog folder for Apache2"
mkdir /var/log/apache2/access
mkdir /var/log/apache2/error

#####################################################################
# Enabling Apache2 modules
#
echo "Enabling Apache2 modules"
yes | rm /etc/apache2/mods-enabled/*
mod_enabled=(alias.conf alias.load auth_basic.load authn_file.load authz_default.load authz_groupfile.load authz_host.load authz_user.load deflate.conf deflate.load dir.conf dir.load env.load jk.load mime.conf mime.load reqtimeout.conf reqtimeout.load setenvif.conf setenvif.load unique_id.load)
for mod in ${mod_enabled[@]}; do
  ln -s /etc/apache2/mods-available/$mod /etc/apache2/mods-enabled/
done

#####################################################################
# Setting Apache host
#
echo "Setting Apache host file"
yes | rm /etc/apache2/sites-enabled/*
yes | cp templates/apache2_host_codefaces /etc/apache2/sites-available/codefaces
yes | ln -s /etc/apache2/sites-available/codefaces /etc/apache2/sites-enabled/

#####################################################################
# Setting Apache2 security file
#
echo "Setting Apache2 security"
yes | cp templates/apache2_security /etc/apache2/conf.d/security

#####################################################################
# Setting IPTables
#
echo "Setting iptables"
mkdir /etc/network/iptables
sed -e 's/\$SSH_PORT/'$SSH_PORT'/g' \
    templates/iptables.rule > /etc/network/iptables/iptables.rule
chmod 640 /etc/network/iptables/iptables.rule
yes | cp templates/iptables_load /etc/network/if-pre-up.d/iptables_load
iptables-restore < /etc/network/iptables/iptables.rule

#####################################################################
# Restart Apach2
#
/etc/init.d/apache2 restart

