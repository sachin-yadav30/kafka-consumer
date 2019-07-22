package com.gracenote.relay.consumer.util;

import com.gracenote.relay.consumer.model.JobVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsumerUtil {
    private static Logger logger = LoggerFactory.getLogger(ConsumerUtil.class);

    public  static JobVO createVO(String startedAt,String finishedAt,String startedBy,String tokenName,String jobId,String jobType){
        JobVO jobVO=new JobVO();
        jobVO.setStartedAt(startedAt);
        jobVO.setFinishedAt(finishedAt);
        jobVO.setStartedBy(startedBy);
        jobVO.setTokenName(tokenName);
        jobVO.setJobId(jobId);
        jobVO.setJobType(jobType);
        return jobVO;
    }

    public static String createDate(){
        Date date = new Date();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        return strDate;
    }
}
