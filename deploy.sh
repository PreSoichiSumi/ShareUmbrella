#!/bin/sh
set -e -x
sudo apt-get update -qq
sudo apt-get install sshpass -qq
eval `ssh-agent -s`
openssl aes-256-cbc -K $encrypted_6aecf171dcc3_key -iv $encrypted_6aecf171dcc3_iv -in data.pem.enc -out data.pem -d
ls -al
sudo ssh-keygen -R ec2-54-238-225-208.ap-northeast-1.compute.amazonaws.com
sudo chmod 400 data.pem
sudo -E scp -Bv -i data.pem shareUmbrella.war ec2-user@ec2-54-238-225-208.ap-northeast-1.compute.amazonaws.com:/home/ec2-user
sudo -E sshpass -p shareumbrella ssh -i data.pem root@ec2-54-238-225-208.ap-northeast-1.compute.amazonaws.com
cd /home/ec2-user
sudo rm -rd /var/lib/tomcat7/shareUmbrella
sudo mv shareUmbrella.war /var/lib/tomcat7/webapps/
sudo service tomcat7 restart
