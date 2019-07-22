# RelayBlobConsumer (How to run this consumer jar)

  - build the damn thing using jenkins JobName : RelayBlobConsumer_RELEASE 
  - Jenkins Job will deploy the jar to /relay/udisConsumer/builds/latest/ on relayetl01 server
  - The jar will be deployed as a service To run this service use the following command:
       - service udisConsumer {status|start|stop|restart}
  - The logs will be available in /relay/logs/RelayConsumer.log  
      
# To run the consumer on local server
  - build the damn thing: mvn clean install (this will create executable jar)
  - run the jar from target/RelayBlobConsumer-1.0.jar using following command:
    - java -jar RelayBlobConsumer-1.0.jar >/dev/null 2>&1 &
    - The log will be written in /relay/logs/RelayConsumer.log

The running consumer will download files from kafka topic
The file will be downloaded into the token /in directory
If the token directory is missing the code will create the directory for token and download the files to respective /in directory.


