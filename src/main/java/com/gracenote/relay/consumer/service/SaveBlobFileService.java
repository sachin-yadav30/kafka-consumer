package com.gracenote.relay.consumer.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.gracenote.relay.consumer.model.JobVO;
import com.gracenote.relay.consumer.util.ConsumerUtil;
import org.apache.log4j.Logger;

public class SaveBlobFileService {
	private static Logger logger=Logger.getLogger(SaveBlobFileService.class);
    ConsumerJobApiRestService consumerJobApiRestService=new ConsumerJobApiRestService();
	public int saveBlobfile(ByteBuffer buffer, String filePathName, JobVO jobVO,String fileName) throws IOException {
		
		logger.info("Write process of blob file initiated");
		
		FileOutputStream fileOutputStream=null;
		try{
			File file = new File(filePathName);
			fileOutputStream=new FileOutputStream(file, false);
			FileChannel wChannel = fileOutputStream.getChannel();
			wChannel.write(buffer);
			wChannel.close();
            jobVO.setFinishedAt(ConsumerUtil.createDate());
            jobVO.setJobType("ENDGETTER");
            String response=consumerJobApiRestService.saveOrUpdateConusmerJob(jobVO,fileName);
            logger.info("Response after updating consumer job."+response);
            Runtime.getRuntime().exec("/relay/etl/bin/startetl -t "+jobVO.getTokenName());
		}
		finally {
			if(fileOutputStream != null)
				fileOutputStream.close();
		}
		
		logger.info("Write process of blob file completed successfully.");
		return 0;
	}
}
