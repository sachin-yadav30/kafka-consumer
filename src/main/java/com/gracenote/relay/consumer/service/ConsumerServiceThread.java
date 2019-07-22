package com.gracenote.relay.consumer.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;

import com.gracenote.relay.consumer.exceptions.KafkaTopicNotFoundException;
import com.gracenote.relay.consumer.model.Config;
import org.omg.CORBA.INTERNAL;

public class ConsumerServiceThread {
	private static Logger logger=Logger.getLogger(ConsumerServiceThread.class);
	ExecutorService executorServiceObj = Executors.newFixedThreadPool(Integer.parseInt(PropertyFileReaderService.blobListenThreadCount));
	public void consumerThread(final String blobTenantId) {
		
			executorServiceObj.submit( () -> {
			
			StringWriter sw = new StringWriter();

			
			String blobKafkaTopicName=Config.BLOB_KAFKA_TOPIC_NAME_CONSTANT+blobTenantId+"-UDIS_RelayApp";
			String consumerGrp=blobKafkaTopicName+Config.BLOB_KAFKA_CONSUMER_GROUP_CONSTANT;
			try {
				ConsumerService consumer = new ConsumerService(blobKafkaTopicName, consumerGrp);
				consumer.consumeMessageService();
			}
			catch(KafkaTopicNotFoundException e) {
				logger.error("KafkaTopicNotFoundException occured. "+ e.getMessage());
				
				logger.debug(Config.DEBUG_MESSAGE);
				e.printStackTrace(new PrintWriter(sw));
				logger.debug(sw.toString());
				
				logger.error("Parser Consumer failed for Topic name: "+blobKafkaTopicName);
			}
			catch(Exception e) { 
				logger.error("Exception occured. "+ e.getMessage());
				
				logger.debug(Config.DEBUG_MESSAGE);
				e.printStackTrace(new PrintWriter(sw));
				logger.debug(sw.toString());
				
				logger.error("Parser Consumer failed for Topic name: "+blobKafkaTopicName);
			} 
			
		});
	}
}
