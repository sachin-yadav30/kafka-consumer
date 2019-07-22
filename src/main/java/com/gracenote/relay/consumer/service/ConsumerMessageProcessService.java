package com.gracenote.relay.consumer.service;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.gracenote.relay.consumer.model.JobVO;
import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.gracenote.relay.consumer.exceptions.ConsumerMessageProcessException;
import com.gracenote.relay.consumer.model.Config;

public class ConsumerMessageProcessService {
	private static Logger logger=Logger.getLogger(ConsumerMessageProcessService.class);
//	FileMetadataService fileMetadataService=new FileMetadataService();
//	CommandCreatorService commandCreatorService=new CommandCreatorService();
//	CommandExecutorService commandExecService=new CommandExecutorService();
	

	/**
	 * Following method responsible for writing the file at specified location 
	 * or Download the file and then write the file at specified location followed by database update process.
	 * Also responsible for fetching file metadata, creating parser command and executing parser command. 
	 * @param jobId
	 * @param buffer
	 * @param fileUrl
	 * @param blobInputType
	 * @param blobOutputType
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws ConsumerMessageProcessException 
	 */
	public void processConsumeMessage(String tokenName, ByteBuffer buffer, String fileUrl, JobVO jobVO) throws IOException, InterruptedException, ExecutionException, ConsumerMessageProcessException{
		ExecutorService executorServiceObj = Executors.newFixedThreadPool(Integer.parseInt(PropertyFileReaderService.fileWritingWorkerThreadCount));
		int fileSaveStatus=1;
		JsonObject jsonObj;
		String command;
		String fileNameToSave=tokenName+"_"+new Date().getTime()+".zip";
		Future<Integer> fileSaveFuture = null;
		
		File tmpDir = new File(Config.FILE_SAVEDIR + tokenName+"/data/in/");
		boolean exists = tmpDir.exists();
		if(!exists) {
			logger.info("INSIDE NOT EXITS");
			Runtime.getRuntime().exec("sh /relay/provider/providerDirSetup.sh "+tokenName);	
			Thread.sleep(5000);
		}
		logger.info("Initiated processing of Download Consumed Message");
		if(buffer != null && fileUrl == null)
		{
			fileSaveFuture=executorServiceObj.submit(new SaveBlobFileServiceThread(tokenName,buffer,fileNameToSave,jobVO));
		
		}
		else if(buffer == null && fileUrl != null)
		{
			fileSaveFuture=executorServiceObj.submit(new DownloadAndSaveBlobFileServiceThread(tokenName,fileUrl,fileNameToSave,jobVO));
		}
	}
}
