import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.model.UploadPartResult;

public class UploadPart{

    private static File createSampleFile() throws IOException {
        File file = File.createTempFile("aws-java-sdk-", ".txt");
        file.deleteOnExit();

        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write("abcdefghijklmnopqrstuvwxyz\n");
        writer.write("01234567890112345678901234\n");
        //writer.write("!@#$%^&*()-=[]{};':',.<>/?\n");
        writer.write("01234567890112345678901234\n");
        writer.write("abcdefghijklmnopqrstuvwxyz\n");
        writer.close();

        return file;
    }
    
	private static void basicUploadPart() throws IOException
	{
		System.out.println("basic Upload Part");
		String bucketName="region";
		String fileName="world.txt";
		//String md5Digest="aSsJ8P/c05f2r0JDoSWbHg==";
		String uploadID = "4Q4P412QUQQPOCLDD6PU2S6ERLNW62MGLZ6O1SJWJ55QEBIT9B5WON13DG"; //hello
		//String uploadID = "XHGTFV4F5XTEAC5O8N3LK12TIY3DSY7OFPXIWTHRMNTE7A3WB5M8N2U5AN"; //hi
		
		UploadPartRequest config = new UploadPartRequest();
		config.setBucketName(bucketName);
		config.setKey(fileName);
		config.setPartNumber(1); //part number
		config.setUploadId(uploadID);
		config.setFile(createSampleFile());
		config.setPartSize(108); //content-length
		//config.setMd5Digest(md5Digest);
		//config.setLastPart(true);

		
		AmazonS3 s3 = new AmazonS3Client(new PropertiesCredentials(putBucket.class.getResourceAsStream("AwsCredentials.properties")));
		try
		{
			UploadPartResult result = s3.uploadPart(config);
			System.out.println(result.getPartNumber());
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
		basicUploadPart();
	}
		
}