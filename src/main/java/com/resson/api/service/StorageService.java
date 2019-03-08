package com.resson.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.nio.charset.StandardCharsets.UTF_8;


//import com.google.api.client.util.DateTime;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Blob.BlobSourceOption;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;
import org.apache.commons.codec.binary.Base64;

public class StorageService {

  final Logger logger = LoggerFactory.getLogger(com.resson.api.controller.FieldController.class);

  // private static final String DatePattern = "dd-MM-yyyy-HH-mm";
  private static Storage storage = null;

  // [START init]
  public void init() {
    storage = StorageOptions.getDefaultInstance().getService();
  }
  // [END init]

  public String getBase64FromBlob(String fileName) {
    
    byte[] encodedBytes = new byte[0];

    // example fileName string:
    // bucketname-fieldname
    // so from arrOfParams:
    // arrOfParams[0] = bucket name
    // arrOfParams[1] = field name

    Blob blob = null;       
    String[] arrOfParams = fileName.split("~", 2); 
    String bucketName = arrOfParams[0];
    BlobId bId = BlobId.of(bucketName, fileName);


    // with generation == null, it gets the latest gen of resource
    try{

        blob = storage.get(bId);
        // Get object content
        byte[] content = blob.getContent(BlobSourceOption.generationMatch());
        // byte array to string
        String s = new String(content);
        encodedBytes = Base64.encodeBase64(s.getBytes(UTF_8));
     
    }catch (StorageException e){
      logger.error("getBase64FromBlob error : {} : {}", e.getMessage(), e.getStackTrace());
    } finally {
      
    }

    return new String(encodedBytes);
  }

  public Blob getBlob(String fileName) {
    
    byte[] encodedBytes = new byte[0];

    // example fileName string:
    // bucketname-fieldname
    // so from arrOfParams:
    // arrOfParams[0] = bucket name
    // arrOfParams[1] = field name

    Blob blob = null;  
    BlobId bId = null;     
    String[] arrOfParams = fileName.split("~", 2); 
    String bucketName = arrOfParams[0];


    // with generation == null, it gets the latest gen of resource
    try{

        bId = BlobId.of(bucketName, fileName);
        blob = storage.get(bId);
       
     
    }catch (Exception e){
      logger.error("getBlob error : {} : {}", e.getMessage(), e.getStackTrace());
    } finally {
      
    }

    return blob;
  }

  public Blob setBlobFromBase64(String fileName, String fileContents){

    // example fileName string:
    // bucketname-fieldname
    // so from arrOfParams:
    // arrOfParams[0] = bucket name
    // arrOfParams[1] = field name

    Blob blob = null;
    String[] arrOfParams = fileName.split("~", 2); 
    String bucketName = arrOfParams[0];
    BlobId blobId = BlobId.of(bucketName, fileName);
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
    blob = storage.create(blobInfo, fileContents.getBytes(UTF_8));
    
    return blob;

  }


}