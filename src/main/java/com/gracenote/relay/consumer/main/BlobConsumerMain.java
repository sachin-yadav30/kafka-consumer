package com.gracenote.relay.consumer.main;

import org.apache.log4j.BasicConfigurator;

import com.gracenote.relay.consumer.controller.ConsumerController;

public class BlobConsumerMain {

	public static void main( String[] args )
    {
		//BasicConfigurator.configure();
    	new ConsumerController().consumerController();
    }
}
