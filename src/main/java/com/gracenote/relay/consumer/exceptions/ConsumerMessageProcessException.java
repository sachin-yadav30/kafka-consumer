package com.gracenote.relay.consumer.exceptions;

public class ConsumerMessageProcessException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	private String message = "Failure in processing Consumed Message for Parser.";
	
	public ConsumerMessageProcessException()
	{ 
		super();
	}

	public ConsumerMessageProcessException(String message)
	{
		super(message);
		this.message = message;
		
	}
	 
	@Override
	public String getMessage() {
		return message;
	}
}