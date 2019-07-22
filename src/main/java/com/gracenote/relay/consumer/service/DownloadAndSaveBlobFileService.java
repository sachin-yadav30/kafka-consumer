package com.gracenote.relay.consumer.service;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.gracenote.relay.consumer.model.JobVO;
import com.gracenote.relay.consumer.util.ConsumerUtil;
import org.apache.log4j.Logger;

import com.gracenote.relay.consumer.model.Config;

public class DownloadAndSaveBlobFileService {
	private static Logger logger=Logger.getLogger(DownloadAndSaveBlobFileService.class);
	HttpURLConnection httpConn;
	InputStream inputStream;
	FileOutputStream outputStream;
    ConsumerJobApiRestService consumerJobApiRestService=new ConsumerJobApiRestService();

	public int downloadAndSaveBlobfile(String fileUrl, String filePathName, JobVO jobVO) throws IOException{
		int downloadAndSaveStatus=1;
		
		logger.info("File download process initiated with download url as: "+ fileUrl+ " and filePathName value as: "+filePathName);
		try
		{
			URL url = new URL(fileUrl.trim());
	        httpConn = (HttpURLConnection) url.openConnection();
	        // set request method
	        httpConn.setRequestMethod(Config.GET_METHOD);
	        //add request header
            httpConn.setRequestProperty(Config.BLOBTYPE, "UDIS_RelayApp");
	        int responseCode = httpConn.getResponseCode();
	        
	        // always check HTTP response code first
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            String fileName = "";
	            String disposition = httpConn.getHeaderField(Config.CONTENT_DISPOSITION);
	            String contentType = httpConn.getContentType();
	            long totalBytesRead = 0;
	 
	            if (disposition != null) {
	                // extracts file name from header field
	                int index = disposition.indexOf(Config.FILENAME);
	                if (index > 0) {
	                    fileName = disposition.substring(index + 10,
	                            disposition.length() - 1);
	                }
	            } else {
	                // extracts file name from URL
	                fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1,
	                		fileUrl.length());
	            }
	 
	            logger.info("File Content-Type = " + contentType);
	            logger.info("File Content-Disposition = " + disposition);
	            logger.info("File Name = " + fileName);
	 
	            // opens input stream from the HTTP connection
	            inputStream = httpConn.getInputStream();
	            
	            // opens an output stream to save into file
	            outputStream = new FileOutputStream(filePathName);
	            
	            int bytesRead = -1;
	            byte[] buffer = new byte[Config.BUFFER_SIZE];
	            while ((bytesRead = inputStream.read(buffer)) != -1) {
	                outputStream.write(buffer, 0, bytesRead);
	                totalBytesRead+= bytesRead;
	            }
	 
	            if(totalBytesRead != 0)
	            	logger.info("File Content-Length = " + (totalBytesRead / 1024)+ " Kbs");
	            logger.info("File downloaded successfully.");
	            logger.info("File download process with download url as: "+ fileUrl +" completed successfully.");
	            downloadAndSaveStatus=0;

                jobVO.setFinishedAt(ConsumerUtil.createDate());
                jobVO.setJobType("ENDGETTER");
                String response=consumerJobApiRestService.saveOrUpdateConusmerJob(jobVO,fileName);
                logger.info("Response after updating consumer job."+response);
                Runtime.getRuntime().exec("/relay/etl/bin/startetl -t "+jobVO.getTokenName());
	        } else {
	        	logger.error("No file to download. Server replied HTTP response message as : " + responseCode +" "+ httpConn.getResponseMessage());
	        	
	        	BufferedReader rd = new BufferedReader(new InputStreamReader(httpConn.getErrorStream()));
				StringBuilder errorResponse= new StringBuilder();
				String line = "";
				while ((line = rd.readLine()) != null) {
					errorResponse.append(line);
				}
				logger.error("Error response from Blob is: "+errorResponse);
				
				logger.error("Failure in File download process with download url as: "+ fileUrl);
	        }
       
		}
		finally {
			if(outputStream != null)
				outputStream.close();
			if(inputStream != null)
				inputStream.close();
			if(httpConn != null)
				httpConn.disconnect();
		}
		
		return downloadAndSaveStatus;
	}
}
