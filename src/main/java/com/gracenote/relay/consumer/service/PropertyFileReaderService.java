package com.gracenote.relay.consumer.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.gracenote.relay.consumer.exceptions.ValidationException;
import com.gracenote.relay.consumer.model.Config;

public class PropertyFileReaderService {
	private static Logger logger=Logger.getLogger(PropertyFileReaderService.class);
	
	static String blobTenantId;
	static String blobBaseUrl;
	static String blobUrl;
	static String kafkaBootstrapServers;
	static String kafkaSchemaRegistryUrl;
    public static String blobListenThreadCount;
    public static String fileWritingWorkerThreadCount;
    public static String dashboardUrl;
	public static void readPropertyFileData() throws IOException, ValidationException{
		logger.info("Reading ConsumerMapper.properties file");
		/*For Local testing running through eclipse*/
		FileInputStream inputStream = new FileInputStream(new File(Config.APP_PROPERTY_FILE_PATH_LOCAL));
		
		/*While building Maven Jar and deploying on Vagrant/Server*/ 
		//InputStream inputStream = PropertyFileReaderService.class.getResourceAsStream(Config.APP_PROPERTY_FILE_PATH_SERVER);
		Properties properties = new Properties();
		properties.load(inputStream);
		inputStream.close();
		
		blobTenantId=properties.getProperty(Config.BLOB_TENANT_ID);
		blobBaseUrl=properties.getProperty(Config.BLOB_BASEURL);
		kafkaBootstrapServers=properties.getProperty(Config.KAFKA_BOOTSTRAP_SERVERS);
		kafkaSchemaRegistryUrl=properties.getProperty(Config.KAFKA_SCHEMA_REGISTRY_URL);
        blobListenThreadCount=properties.getProperty(Config.Blob_Listen_Thread_Count);
        fileWritingWorkerThreadCount=properties.getProperty(Config.File_Writing_Worker_Thread_Count);
        dashboardUrl=properties.getProperty(Config.Relay_Dashboard_BASE_URL);
		
		new PropertyFileReaderService().vaildateData();
		
		logger.info("Blob Tenant Id mapped in a property file is: "+ blobTenantId);
		blobTenantId=blobTenantId.trim();
		
		logger.info("Blob Base-URL mapped in a property file is: "+ blobBaseUrl);
		blobBaseUrl=blobBaseUrl.trim();
		blobUrl=blobBaseUrl.trim()+"/tenants/"+blobTenantId.trim()+"/blobs";
		
		logger.info("KafkaBootstrap Server details mapped in a property file is: "+ kafkaBootstrapServers);
		kafkaBootstrapServers=kafkaBootstrapServers.trim();
		
		logger.info("KafkaSchemaRegistry url mapped in a property file is: "+ kafkaSchemaRegistryUrl);
		kafkaSchemaRegistryUrl=kafkaSchemaRegistryUrl.trim();
	}
	
	private void vaildateData() throws ValidationException{

		if(!nullAndEmptyCheck(blobBaseUrl))
		{
			throw new ValidationException("Blob Base-Url not provided in ConsumerMapper Property file, expected to provide valid blob base-url.");
		}
		
		if(!nullAndEmptyCheck(kafkaBootstrapServers))
		{
			throw new ValidationException("KafkaBootstrap server details not provided in ConsumerMapper Property file, expected to provide valid KafkaBootstrap server details.");
		}
		
		if(!nullAndEmptyCheck(kafkaSchemaRegistryUrl))
		{
			throw new ValidationException("KafkaSchemaRegistry url not provided in ConsumerMapper Property file, expected to provide valid KafkaSchemaRegistry url.");
		}
	}

	private boolean nullAndEmptyCheck(String propertyFileData) {
		
		boolean isDataPresent=false;
		if(propertyFileData != null && !propertyFileData.isEmpty())
		{
			isDataPresent=true;
		}
		
		return isDataPresent;
	}
	
	public static void setBlobUrl(String blobUrl) {
		PropertyFileReaderService.blobUrl = blobUrl;
	}
}
