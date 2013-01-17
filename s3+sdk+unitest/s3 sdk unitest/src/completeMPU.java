import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.PartETag;

public class completeMPU{
	
	private static void basicCompleteMPU() throws IOException
	{
		System.out.println("basic complete MPU");
		String bucketName="chttest";
		String fileName="hello.txt";
		String uploadID = "LK7JUNJ8IRDDTYD9KWBJ0TN8I04JIWVF9HRQK7TNX4PZ6QY0UQT8JGJK13"; //hello
		List<PartETag> list = new ArrayList<PartETag>(); //etag
		list.add(new PartETag (3,"96f07d52e483bd0efe4d9f6ee4646d7a"));
		//list.add(new PartETag (3,"548270b9bb80feb0c1b851d3442c7abf"));
		
		CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest(bucketName,fileName,uploadID,list);
		
		AmazonS3 s3 = new AmazonS3Client(new PropertiesCredentials(putBucket.class.getResourceAsStream("AwsCredentials.properties")));
		try
		{
			CompleteMultipartUploadResult result = s3.completeMultipartUpload(request);
			System.out.println(result.getBucketName());
			System.out.println(result.getETag());
			System.out.println(result.getKey());
			System.out.println(result.getLocation());
			//System.out.println(result.getParts());
	        System.out.println();
	    }
		catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
	                    + "to Amazon S3, but was rejected with an error response for some reason.");
	        System.out.println("Error Message:    " + ase.getMessage());
	        System.out.println("HTTP Status Code: " + ase.getStatusCode());
	        System.out.println("AWS Error Code:   " + ase.getErrorCode());
	        System.out.println("Error Type:       " + ase.getErrorType());
	        System.out.println("Request ID:       " + ase.getRequestId());
	    } catch (AmazonClientException ace) {
	    	System.out.println("Caught an AmazonClientException, which means the client encountered "
	                    + "a serious internal problem while trying to communicate with S3, "
	                    + "such as not being able to access the network.");
	    	System.out.println("Error Message: " + ace.getMessage());
	    }
	}
	
    public static void main(String args[]) throws IOException
	{
		System.out.println("hello world");
		basicCompleteMPU();
	}
		
}