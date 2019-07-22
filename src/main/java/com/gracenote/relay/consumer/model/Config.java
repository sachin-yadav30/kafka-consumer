package com.gracenote.relay.consumer.model;

public class Config {

	private Config(){}
	
	public static final String APP_PROPERTY_FILE_PATH_LOCAL="/relay/etl/conf/ConsumerMapper.properties";
    //public static final String APP_PROPERTY_FILE_PATH_LOCAL="/home/sayadav/relay-git/relay-blob-consumer/profiles/dev/ConsumerMapper.properties";
	public static final String APP_PROPERTY_FILE_PATH_SERVER="/ConsumerMapper.properties";

    public static final String Blob_Listen_Thread_Count="BlobListenThreadCount";
    public static final String File_Writing_Worker_Thread_Count="FileWritingWorkerThreadCount";
    public static final String Relay_Dashboard_BASE_URL="RelayApiBaseUrl";
    public static final String Relay_API_Job_URL="/saveJobDetails.html";

    public static final String BLOB_TENANT_ID="BlobTenantId";
	public static final String BLOB_BASEURL="BlobBaseUrl";
	public static final String BLOB_KAFKA_TOPIC_NAME_CONSTANT="Blob-Service-";
	public static final String BLOB_KAFKA_CONSUMER_GROUP_CONSTANT="-CG";
	public static final String BLOB_URL="blobUrl";
	public static final String ORG="org";
	public static final String KAFKA="kafka";
	public static final String KAFKA_BOOTSTRAP_SERVERS="KafkaBootstrapServers";
	public static final String KAFKA_SCHEMA_REGISTRY_URL="KafkaSchemaRegistryUrl";
	public static final String DB_API_BASE_URL="DBApiBaseUrl";
	
	public static final String KAFKA_KEY_DESERIALIZER = "org.apache.kafka.common.serialization.LongDeserializer";
	public static final String KAFKA_VALUE_DESERIALIZER = "io.confluent.kafka.serializers.KafkaAvroDeserializer";
	public static final String KAFKA_AUTO_COMMIT_CONFIG_VALUE="true";
	
	public static final String BLOB_METADATATAGS="metadatatags";
	public static final String BLOB_TOKEN_NAME="relayToken";
    public static final String BLOB_JOBID="jobid";
	public static final String BLOB_ISBLOBPRESENT="isBlobPresent";
	public static final String BLOB_FILE="file";
	public static final String BLOB_DOWNLOADURL="downloadUrl";
	
	
	public static final int BUFFER_SIZE = 4096;
	public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";
	public static final String BLOBTYPE = "blobType";
	public static final String CONTENT_DISPOSITION = "Content-Disposition";
	public static final String FILENAME = "filename=";

	/*For Local testing running through eclipse*/
	public static final String FILE_SAVEDIR = "/relay/provider/";
	
	public static final String FAILURE_LOG_MESSAGE="Failure in processing Consumed Message for Parser.";
	public static final String DEBUG_MESSAGE="Below is the stacktrace of the exception.";

	
		
}
