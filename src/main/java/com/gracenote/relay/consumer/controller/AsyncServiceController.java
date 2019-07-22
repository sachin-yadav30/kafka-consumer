package com.gracenote.relay.consumer.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import com.gracenote.relay.consumer.service.PropertyFileReaderService;
import org.apache.avro.generic.GenericRecord;

import com.gracenote.relay.consumer.service.ConsumerMessageDownloadService;
import com.gracenote.relay.consumer.service.ConsumerMessageProcessService;

public class AsyncServiceController {
	ExecutorService executorServiceObj = Executors.newFixedThreadPool(Integer.parseInt(PropertyFileReaderService.blobListenThreadCount));
	public void asyncServiceMethod(final GenericRecord rec)
	{
		executorServiceObj.submit( () -> new ConsumerMessageDownloadService().parseConsumeMessage(rec, new ConsumerMessageProcessService()));
	}
}
