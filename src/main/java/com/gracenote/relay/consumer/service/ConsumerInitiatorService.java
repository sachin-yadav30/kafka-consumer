package com.gracenote.relay.consumer.service;

import org.apache.log4j.Logger;

public class ConsumerInitiatorService {
	private static Logger logger=Logger.getLogger(ConsumerInitiatorService.class);
	
	public void initiateConsumer(ConsumerServiceThread consumerServiceThread){

		consumerServiceThread.consumerThread(PropertyFileReaderService.blobTenantId);	
			
	}
}
