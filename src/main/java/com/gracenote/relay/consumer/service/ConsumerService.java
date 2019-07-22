package com.gracenote.relay.consumer.service;

import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;

import com.gracenote.relay.consumer.controller.AsyncServiceController;
import com.gracenote.relay.consumer.exceptions.KafkaTopicNotFoundException;
import com.gracenote.relay.consumer.model.Config;

import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;

public class ConsumerService {
	
	private static Logger logger=Logger.getLogger(ConsumerService.class);
	private KafkaConsumer<Long, GenericRecord> consumerObj;
	private String topic;
	private AtomicBoolean isRunning = new AtomicBoolean(true);
	AsyncServiceController asyncObj=new AsyncServiceController();
	
	
	public ConsumerService(String topicName, String consumerGrp) {
			
	        Properties props = new Properties();
	        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, PropertyFileReaderService.kafkaBootstrapServers);
		    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, Config.KAFKA_KEY_DESERIALIZER);
		    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, Config.KAFKA_VALUE_DESERIALIZER);
		    props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGrp);
		    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Config.KAFKA_AUTO_COMMIT_CONFIG_VALUE);
		    //props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
		 /* Default AUTO_COMMIT_INTERVAL_MS = 5000 = 5 seconds*/
		 // props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		    props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, PropertyFileReaderService.kafkaSchemaRegistryUrl);
	
		    consumerObj = new KafkaConsumer(props);
		    this.topic = topicName;
	        logger.info("Topic Subscribed for Parser consumer: "+ topic);
	    }
	
	public void consumeMessageService() throws KafkaTopicNotFoundException {
    	
		/**----------------------------------------------------
		 * Check the existence of Relay Consumer Topic
		 *-----------------------------------------------------*/
		if(!consumerObj.listTopics().keySet().contains(topic))
		{
			logger.error("Invalid topic name: "+topic);
			throw new KafkaTopicNotFoundException();
		}
		
        while (isRunning.get()) {
        	consumeMessage();
        }
    
	}
	private void consumeMessage()
	{
		/**----------------------------------------------------
		 * Subscribe to the UDIS topic
		 *-----------------------------------------------------*/
		consumerObj.subscribe(Arrays.asList(new String[]{topic}));

		/**-----------------------------------------------
		 * Consume Avro GenericRecords
		 *------------------------------------------------*/
		ConsumerRecords<Long, GenericRecord> records = consumerObj.poll(1000);

		for(ConsumerRecord<Long, GenericRecord> record : records) {
			GenericRecord genericRecord = record.value();
			logger.info("Consumed Kafka Message Timestamp: "+ record.value());
			logger.info("Consumed Kafka Message: " + genericRecord);
			logger.info("Consumed Kafka Message Offset: "+ record.offset());
			logger.info("Consumed Kafka Message from Partition: "+ record.partition());
			logger.info("Consumed Kafka Message Timestamp: "+ new Date(record.timestamp()));
			
			asyncObj.asyncServiceMethod(genericRecord);

		}
	}
}
