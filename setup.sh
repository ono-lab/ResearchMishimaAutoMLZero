sudo apt update
sudo apt upgrade -y
sudo apt install -y openjdk-17-jdk
sudo apt install -y maven

sudo sh -c 'echo "JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64" >> /etc/environment'
source /etc/environment