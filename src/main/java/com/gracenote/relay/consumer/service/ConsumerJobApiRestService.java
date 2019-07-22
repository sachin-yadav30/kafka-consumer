package com.gracenote.relay.consumer.service;

import com.google.gson.Gson;
import com.gracenote.relay.consumer.model.Config;
import com.gracenote.relay.consumer.model.JobVO;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URLEncoder;

public class ConsumerJobApiRestService {
    private static Logger logger=Logger.getLogger(ConsumerJobApiRestService.class);
    private  static final String apiUrl=PropertyFileReaderService.dashboardUrl+Config.Relay_API_Job_URL;


    public String saveOrUpdateConusmerJob(JobVO jobVO,String fileName)throws IOException {
        Gson g = new Gson();
        String str = g.toJson(jobVO);

        StringEntity entity = new StringEntity(str,
                ContentType.APPLICATION_JSON);
        System.out.println(str);
        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost request;
        if(fileName!=null){
            request = new HttpPost(apiUrl+"?fileName="+URLEncoder.encode(fileName,"UTF-8"));
        }else{
            request = new HttpPost(apiUrl);
        }

        request.setEntity(entity);

        HttpResponse response = httpClient.execute(request);
        logger.info(response.getStatusLine().getStatusCode());

        return "done";
    }

//
//    public static void main(String args[])throws IOException{
//        JobVO jobVO=new JobVO();
//        jobVO.setFinishedAt("text");
//        jobVO.setJobId("123");
//        jobVO.setStartedAt("text");
//        jobVO.setJobType("Getter");
//        jobVO.setTokenName("test");
//        ConsumerJobApiRestService consumerJobApiRestService=new ConsumerJobApiRestService();
//        consumerJobApiRestService.saveOrUpdateConusmerJob(jobVO);
//    }
}
