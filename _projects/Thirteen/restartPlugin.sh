echo "Copying Plugin"

cp .jenkins/jobs/ThirteenIM/workspace/instant-messaging-plugin-master/target/instant-messaging.hpi .jenkins/plugins/instant-messaging.hpi 

pkill -f 'java -jar'

echo "Restarting Jenkins"

java -jar $HOME/jenkins.war --httpPort=-1 --httpsPort=8083 --httpsKeyStore=$HOME/keystore --httpsKeyStorePassword=123456 &
