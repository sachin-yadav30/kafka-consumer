package com.gracenote.relay.consumer.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.Callable;

import com.gracenote.relay.consumer.model.JobVO;
import org.apache.log4j.Logger;

import com.gracenote.relay.consumer.model.Config;

public class DownloadAndSaveBlobFileServiceThread implements Callable<Integer> {
	private static Logger logger=Logger.getLogger(DownloadAndSaveBlobFileServiceThread.class);
	DownloadAndSaveBlobFileService  downloadAndSaveBlobFileService= new DownloadAndSaveBlobFileService();
	String tokenName;
	String fileUrl;
	String fileNameToSave;
	StringWriter sw = new StringWriter();
	JobVO jobVO;
	public DownloadAndSaveBlobFileServiceThread(String tokenName,String fileUrl,String fileNameToSave,JobVO jobVO)
	{
		this.tokenName=tokenName;
		this.fileUrl=fileUrl;
		this.fileNameToSave=fileNameToSave;
        this.jobVO=jobVO;
	}
	
	@Override
	public Integer call() throws Exception {
		int downloadAndSaveStatus=1;
		try 
		{
			downloadAndSaveStatus=downloadAndSaveBlobFileService.downloadAndSaveBlobfile(fileUrl,Config.FILE_SAVEDIR + tokenName+"/data/in/" + fileNameToSave,jobVO);
			
		} catch (IOException e) {
			
			logger.error("IOException occured in file download and save process : "+e.getMessage());
			
			logger.debug(Config.DEBUG_MESSAGE);
			e.printStackTrace(new PrintWriter(sw));
			logger.debug(sw.toString());
			
			logger.error("Failure in File download and save process with download url as: "+ fileUrl);
		}
		return downloadAndSaveStatus;
	}
	
}
