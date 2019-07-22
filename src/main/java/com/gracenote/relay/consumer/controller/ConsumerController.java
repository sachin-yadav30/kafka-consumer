package com.gracenote.relay.consumer.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.gracenote.relay.consumer.exceptions.ValidationException;
import com.gracenote.relay.consumer.model.Config;
import com.gracenote.relay.consumer.service.ConsumerInitiatorService;
import com.gracenote.relay.consumer.service.ConsumerServiceThread;
import com.gracenote.relay.consumer.service.PropertyFileReaderService;

public class ConsumerController {
	private static Logger logger=Logger.getLogger(ConsumerController.class);
	ConsumerInitiatorService consumerInitiatorService=new ConsumerInitiatorService();
	StringWriter sw = new StringWriter();
//	public ConsumerController()
//	{
//		Logger.getLogger(Config.ORG).setLevel(Level.INFO);      
//	    Logger.getLogger(Config.KAFKA).setLevel(Level.INFO);
//	}
	
	public void consumerController()
	{
		try 
		{
	
			logger.info("Initiated reading Property file for fetching Blob-Storage Kafka Topic.");
			PropertyFileReaderService.readPropertyFileData();
			logger.info("Completed fetching data from Property file.");
		
		
		
			consumerInitiatorService.initiateConsumer(new ConsumerServiceThread());
		
		} catch (ValidationException e) {
			logger.error("Exception occured related to ConsumerMapper property file : "+e.getMessage());
			
			logger.debug(Config.DEBUG_MESSAGE);
			e.printStackTrace(new PrintWriter(sw));
			logger.debug(sw.toString());
			
		}  catch (IOException e) {
			logger.error("IO Exception occured. "+e.getMessage());
			
			logger.debug(Config.DEBUG_MESSAGE);
			e.printStackTrace(new PrintWriter(sw));
			logger.debug(sw.toString());
		
		} catch(Exception e) { 
			logger.error("Exception occured. "+ e.getMessage());
			
			logger.debug(Config.DEBUG_MESSAGE);
			e.printStackTrace(new PrintWriter(sw));
			logger.debug(sw.toString());
			
			logger.error("Parser Consumer failed.");
		} 
	}
}
