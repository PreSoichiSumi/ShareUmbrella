#!/bin/sh
set -e -x
sudo apt-get update -qq
sudo apt-get install sshpass -qq
eval `ssh-agent -s`
openssl aes-256-cbc -K $encrypted_6aecf171dcc3_key -iv $encrypted_6aecf171dcc3_iv -in data.pem.enc -out data.pem -d
ls -al
sudo chmod 400 data.pem
sudo -E scp -Bv -i data.pem shareUmbrella.war ec2-user@ec2-54-238-225-208.ap-northeast-1.compute.amazonaws.com:/home/ec2-user
sudo ssh -o StrictHostKeyChecking=no -i data.pem ec2-user@ec2-54-238-225-208.ap-northeast-1.compute.amazonaws.com ./deploy2.sh
