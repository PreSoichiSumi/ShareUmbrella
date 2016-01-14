#!/bin/bash
set -e
eval `ssh-agent -s`
echo openssl
openssl aes-256-cbc -K $encrypted_6aecf171dcc3_key -iv $encrypted_6aecf171dcc3_iv -in data.pem.enc -out data.pem -d
ls -al
chmod 400 data.pem
echo ssh
ssh -o StrictHostKeyChecking=no -i data.pem ec2-user@ec2-54-238-225-208.ap-northeast-1.compute.amazonaws.com echo ok
echo scp
scp -Bv -i data.pem shareUmbrella.war ec2-user@ec2-54-238-225-208.ap-northeast-1.compute.amazonaws.com:/home/ec2-user
echo rm -rd
sshpass -p shareumbrella ssh -i data.pem ec2-user@ec2-54-238-225-208.ap-northeast-1.compute.amazonaws.com sudo rm -rdf /var/lib/tomcat7/shareUmbrella
echo mv
sshpass -p shareumbrella ssh -i data.pem ec2-user@ec2-54-238-225-208.ap-northeast-1.compute.amazonaws.com sudo mv /home/ec2-user/shareUmbrella.war /var/lib/tomcat7/webapps/
echo restart
sshpass -p shareumbrella ssh -i data.pem ec2-user@ec2-54-238-225-208.ap-northeast-1.compute.amazonaws.com sudo service tomcat7 restart
