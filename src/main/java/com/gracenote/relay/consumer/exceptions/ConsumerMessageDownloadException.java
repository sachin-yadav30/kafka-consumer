package com.gracenote.relay.consumer.exceptions;

public class ConsumerMessageDownloadException extends Exception{
private static final long serialVersionUID = 1L;
	
	private String message = "Failure in parsing consumed message from Blob-Kafka.";
	
	public ConsumerMessageDownloadException()
	{ 
		super();
	}

	public ConsumerMessageDownloadException(String message)
	{
		super(message);
		this.message = message;
		
	}
	 
	@Override
	public String getMessage() {
		return message;
	}
}
