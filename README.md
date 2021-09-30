# BDT-Realtime-Twitter-Analysis
CS 523 DE : Big Data Technology



**--SetUp Docker**

**--Pull Docker **
docker pull qduong/cloudera-java8:v1
docker images
docker run --hostname=quickstart.cloudera --privileged=true -t -i b9b6b63a10d0 /usr/bin/docker-quickstart -d
docker exec -it 3be6d11b3011 bash
mkdir project
cd project/

**--SetUp JAVA Path**
export JAVA_HOME=/usr/java/jdk1.8.0_181
export PATH=$PATH:$JAVA_HOME/bin

**--Copy Project TwitterRealTimeAnalysis Package to The Docker**
docker cp TwitterRealTimeAnalysis.zip 3be6d11b3011:/project/

**--Download Python**
wget https://downloads.apache.org/kafka/2.6.2/kafka_2.12-2.6.2.tgz
**--Copy Kafka Package to The Docker **
docker cp kafka_2.12-2.6.2.tgz 3be6d11b3011:/opt/kafka

**--Download Python**
wget https://www.python.org/downloads/release/python-2718/Python-2.7.12.tgz
**--Copy Python Package to The Docker **
docker cp Python-2.7.12.tgz 3be6d11b3011:/project/


**--Install and Configure Kafka Server**
cd opt/kafka
sudo tar xvf kafka_2.11–1.1.1.tgz -–directory /opt/kafka -–strip 1
**--Kafka stores it logs on disk in /tmp directory, it is better to create a new directory to store logs**
sudo mkdir /var/lib/kafka
sudo mkdir /var/lib/kafka/data
**--Configure Kafka**
**--Now, we need to edit the Kafka server configuration file.**
sudo vi /opt/kafka/config/server.properties
**--By default, Kafka does not allow us to delete topics. To be able to delete topics, find the line and change it (if it is not found just add it).**
delete.topic.enable = true
**--Now, we need to change the log directory:**
log.dirs=/var/lib/kafka/data

**--Install python**
docker exec -it 3be6d11b3011 bash
cd project
tar xvf Python-2.7.12.tgz
cd Python-2.7.12
./configure
make install

**--Install pip**
python -m ensurepip --upgrade
pip install textblob=0.9.0

**--Install nltk**
pip install nltk=3.0

**--UNPACKING The Project**
docker exec -it 3be6d11b3011 bash
cd project
unzip TwitterRealTimeAnalysis.zip


**--Configuring the Flume Agent in docker**
--Copy Custom Source JAR(TwitterRealTimeAnalysis/Flume/twitter-source/lib/flumetwittersource-0.0.1-SNAPSHOT.jar) to /etc/flume-ng/plugins.d/twitter-source/lib
--Add the Twitter4j dependency JAR(TwitterRealTimeAnalysis/Flume/twitter-interceptor/libext/twitter4j-stream-3.0.6.jar) to /etc/flume-ng/plugins.d/twitter-interceptor/libext
--Copy Custom Interceptor JAR(TwitterRealTimeAnalysis/Flume/twitter-interceptor/lib/flumetwitterinterceptor-0.0.1-SNAPSHOT.jar) to /etc/flume-ng/plugins.d/twitter-interceptor/lib
--Add the json dependency JAR(TwitterRealTimeAnalysis/Flume/twitter-interceptor/libext/json-20180813.jar) to /etc/flume-ng/plugins.d/twitter-interceptor/libext

**--Install Flask**
python -m pip install virtualenv
python -m venv venv
source venv/bin/activate
pip install Flask
flask --version


**--Start Flume**
flume-ng agent --conf /etc/flume-ng/conf --conf-file /project/TwitterRealTimeAnalysis/Flume/flume_twitter_to_kafka.conf --name KafkaAgent --plugins-path /project/TwitterRealTimeAnalysis/Flume/ -Dflume.root.logger=INFO,console

**--Start Zookeeper server**
cd opt/kafka/kafka_2.12-2.6.2
bin/zookeeper-server-start.sh -daemon config/zookeeper.properties

**--Start Kafka server**
cd opt/kafka/kafka_2.12-2.6.2
bin/kafka-server-start.sh config/server.properties

**--Start Kafka producer**
cd opt/kafka/kafka_2.12-2.6.2
bin/kafka-topics.sh --zookeeper quickstart.cloudera:2181 --create --topic twitter_stream --partitions 1 --replication-factor 1

**--Start Kafka consumer**
cd opt/kafka/kafka_2.12-2.6.2
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic twitter_stream --from-beginning

**--Start Dashboards**
cd /project/TwitterRealTimeAnalysis/Dashboard/TwitterAnalysisDashboard
flask run -p 5001

**--Submiting the Spark Job**
cd /project/TwitterRealTimeAnalysis/SparkStreaming/TwitterStreamAnalysis
spark-submit --master 'local[3]' --jars /project/TwitterRealTimeAnalysis/SparkStreaming/jars trending_topics_sentiment.py twitter_stream

Reference Article Used:
http://davidiscoding.com/real-time-twitter-sentiment-analysis-pt-1-introduction


Thank You!
!!!--------ENJOY-------!!!
