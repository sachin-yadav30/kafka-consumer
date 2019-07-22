package com.gracenote.relay.consumer.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

import com.gracenote.relay.consumer.model.JobVO;
import com.gracenote.relay.consumer.util.ConsumerUtil;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gracenote.relay.consumer.exceptions.ConsumerMessageDownloadException;
import com.gracenote.relay.consumer.exceptions.ConsumerMessageProcessException;
import com.gracenote.relay.consumer.model.Config;

public class ConsumerMessageDownloadService {
	private static Logger logger=Logger.getLogger(ConsumerMessageDownloadService.class);
	ConsumerJobApiRestService consumerJobApiRestService=new ConsumerJobApiRestService();
	public void parseConsumeMessage(GenericRecord rec, ConsumerMessageProcessService consumerMessageProcessServiceObj) {
		Object metaData;
		boolean containsBlob;
		JsonParser parser = new JsonParser();
		JsonObject jsonObj;
		StringWriter sw = new StringWriter();
		String  tokenName;
		String jobId;
		try
		{
			logger.info("Initiated parsing of Consumed Message from Blob-kafka");

			metaData=rec.get(Config.BLOB_METADATATAGS);

			if(metaData != null){
				jsonObj = parser.parse(metaData.toString().replaceAll(":","-")).getAsJsonObject();
				if(jsonObj.get(Config.BLOB_JOBID) != null && jsonObj.get(Config.BLOB_TOKEN_NAME)!=null ){
                    tokenName=jsonObj.get(Config.BLOB_TOKEN_NAME).toString().replaceAll("\"", "");
					jobId=jsonObj.get(Config.BLOB_JOBID).toString().replaceAll("\"", "");
					logger.info("Retrieved Token Name from Metadata tag is: " + tokenName);
					containsBlob = (Boolean) rec.get(Config.BLOB_ISBLOBPRESENT);
                    logger.info("Hitting job save end point.");
                    JobVO jobVO=ConsumerUtil.createVO(ConsumerUtil.createDate(),null,"RelayBlobConumser",tokenName,jobId,"STARTGETTER");
                    String response=consumerJobApiRestService.saveOrUpdateConusmerJob(jobVO,null);
                    logger.info("Response from job save end point "+ response);
					if(containsBlob)
					{
						logger.info("Blob-Kafka message consist of entire File.");
						GenericData.Array<ByteBuffer> bytes = (GenericData.Array<ByteBuffer>) rec.get(Config.BLOB_FILE);
						ByteBuffer buffer = bytes.get(0);

						consumerMessageProcessServiceObj.processConsumeMessage(tokenName, buffer,null,jobVO);
					}
					else
					{
						logger.info("Blob-Kafka message consist of File Url.");
						if(rec.get(Config.BLOB_DOWNLOADURL) != null)
						{
							String fileUrl = rec.get(Config.BLOB_DOWNLOADURL).toString();
							logger.info("Retrieved File Url from Blob-Kafka message is: " + fileUrl);

							fileUrl=PropertyFileReaderService.blobBaseUrl+fileUrl;

							logger.info("Parsing of Consumed Message from Blob-kafka completed successfully.");
							consumerMessageProcessServiceObj.processConsumeMessage(tokenName,null,fileUrl,jobVO);
						}
						else
						{
							logger.error("No File Url found from DownloadUrl tag in Blob-Kafka message");
							throw new ConsumerMessageProcessException();
						}
					}
				}
				else
				{
					logger.error("No Job-Id OR Token Name found from Metadata tag in Blob-Kafka message");
					throw new ConsumerMessageDownloadException();
				}
			}
			else
			{
				logger.error("No Metadata tag present in Blob-Kafka message");
				throw new ConsumerMessageDownloadException();
			}

		}catch(ConsumerMessageDownloadException | ConsumerMessageProcessException e) {

			logger.error("Exception occured while parsing/processing consumed message for Parser. "+ e.getMessage());

			logger.debug(Config.DEBUG_MESSAGE);
			e.printStackTrace(new PrintWriter(sw));
			logger.debug(sw.toString());

			logger.error("Download Consumer Message failed.");
		}catch(IOException | ExecutionException | InterruptedException e) {

			logger.error("Exception occured while processing consumed message for Parser. "+ e.getMessage());

			logger.debug(Config.DEBUG_MESSAGE);
			e.printStackTrace(new PrintWriter(sw));
			logger.debug(sw.toString());

			logger.error(Config.FAILURE_LOG_MESSAGE);

			if(e.getClass() == InterruptedException.class)
				Thread.currentThread().interrupt();
		}
		catch(Exception e) {

			logger.error("Exception occured. "+ e.getMessage());

			logger.debug(Config.DEBUG_MESSAGE);
			e.printStackTrace(new PrintWriter(sw));
			logger.debug(sw.toString());

			logger.error("Parser Consumer failed.");

		}
	}
//	public static void main(String args[]){
//
//        Object metaData="{jobid: 76341, fileName: 76341_13:16:49.zip}";
//
//        JsonParser parser = new JsonParser();
//        JsonObject jsonObj;
//        jsonObj = parser.parse(metaData.toString()).getAsJsonObject();
//        System.out.println(jsonObj.get(Config.BLOB_JOBID));
//    }
}
