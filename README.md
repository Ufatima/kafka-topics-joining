# kafka-streaming-application
The application contain 3 topics and using Kafka streams join those 3 topics to a fourth topic with the name elisa-topic

## Getting Started
To run this application, run the following commands:

Open the terminal from the project folder and run the following command to start the containers that we have set in the docker-compose.yml script.
```bash
docker-compose -f docker-compose.yml up -d
```

Get into the Kafka container by executing the below command:
```bash
docker exec -it kafka /bin/sh
```

Execute the kafka-topics.sh script to create topics using the command below.

```bash
kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic first-topic
```
```bash
kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic second-topic
```
```bash
kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic third-topic
```
```bash
kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic elisa-topic
```
Once you have executed the above command, the name of the topic should be returned. To check your topics, run:
```bash
kafka-topics.sh --list --zookeeper zookeeper:2181
```

Send messages from the Producer to the first, second and third input topics by invoking URL http://localhost:8081/topicProduce/

Start the processor by invoking URL http://localhost:8082/topicsJoin/

Output 
To check the output run the command below.
```bash
/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic elisa-topic --property print.key=true --property print.timestamp=true
```


