package com.gracenote.relay.consumer.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

import com.gracenote.relay.consumer.model.JobVO;
import org.apache.log4j.Logger;

import com.gracenote.relay.consumer.model.Config;

public class SaveBlobFileServiceThread implements Callable<Integer>{
	
	private static Logger logger=Logger.getLogger(SaveBlobFileServiceThread.class);
	SaveBlobFileService  saveBlobFileService= new SaveBlobFileService();
	ByteBuffer buffer;
	String fileNameToSave;
	String tokenName;
	JobVO jobVO;
	StringWriter sw = new StringWriter();
	
	public SaveBlobFileServiceThread(String tokenName, ByteBuffer buffer, String fileNameToSave, JobVO jobVO)
	{
		this.tokenName=tokenName;
		this.buffer=buffer;
		this.fileNameToSave=fileNameToSave;
		this.jobVO=jobVO;
	}

	
	@Override
	public Integer call() throws Exception {
		int saveFileStatus=1;
		try 
		{
			saveFileStatus=saveBlobFileService.saveBlobfile(buffer,Config.FILE_SAVEDIR+tokenName+"/data/in/" + fileNameToSave,jobVO,fileNameToSave);
			
		} catch (IOException e) {
			
			logger.error("IOException occured while writing blob file : "+e.getMessage());
			
			logger.debug(Config.DEBUG_MESSAGE);
			e.printStackTrace(new PrintWriter(sw));
			logger.debug(sw.toString());

			logger.error("Process of writing blob file failed.");
		}
		return saveFileStatus;
	}
	

}
