#!/bin/sh
cd /home/ec2-user
sudo rm -rd /var/lib/tomcat7/shareUmbrella
sudo mv shareUmbrella.war /var/lib/tomcat7/webapps/
sudo service tomcat7 restart
