package com.gracenote.relay.consumer.exceptions;

public class KafkaTopicNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String message = "No such Topic exist in Confluent-Kafka Server.";
	
	public KafkaTopicNotFoundException()
	{ 
		super();
	}

	public KafkaTopicNotFoundException(String message)
	{
		super(message);
		this.message = message;
		
	}
	 
	@Override
	public String getMessage() {
		return message;
	}
}
