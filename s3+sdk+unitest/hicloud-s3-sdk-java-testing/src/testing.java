import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ListMultipartUploadsRequest;
import com.amazonaws.services.s3.model.MultipartUpload;
import com.amazonaws.services.s3.model.MultipartUploadListing;
import com.amazonaws.util.DateUtils;

public class testing{
	
	private static int ListMPUs(String bucketName, String IDMarker) throws IOException 
	{
		//System.out.println("list MPUs with Upload ID Marker");
		int count=0;
		
		ListMultipartUploadsRequest request = new ListMultipartUploadsRequest(bucketName);
	
		AmazonS3 s3 = new AmazonS3Client(new PropertiesCredentials(putBucket.class.getResourceAsStream("AwsCredentials.properties")));
		try
		{
			MultipartUploadListing result = s3.listMultipartUploads(request);
			for(MultipartUpload s : result.getMultipartUploads())
			{
				count++;
				System.out.println("count: "+ count);
				System.out.println(s.getKey());
				//System.out.println(s.getUploadId());
			}
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
		return count;
	}
	
	private static void ListMPUs() throws IOException
	{
		String bucketName="chttest2";
		String fileName1="photos/2006/January/sample.jpg";
		String fileName2="photos/2006/February/sample.jpg"; 
		String fileName3="photos/2006/March/sample.jpg"; 
		String fileName4="videos/2006/March/sample.wmv"; 
		String fileName5="sample.jpg"; 
		int count=0;
		
		InitiateMultipartUploadRequest config1 = new InitiateMultipartUploadRequest(bucketName,fileName1);
		InitiateMultipartUploadRequest config2 = new InitiateMultipartUploadRequest(bucketName,fileName2);
		InitiateMultipartUploadRequest config3 = new InitiateMultipartUploadRequest(bucketName,fileName3);
		InitiateMultipartUploadRequest config4 = new InitiateMultipartUploadRequest(bucketName,fileName4);
		InitiateMultipartUploadRequest config5 = new InitiateMultipartUploadRequest(bucketName,fileName5);

		AmazonS3 s3 = new AmazonS3Client(new PropertiesCredentials(putBucket.class.getResourceAsStream("AwsCredentials.properties")));
		
		try
		{
			System.out.println("Creating bucket"+ "\n");
            s3.createBucket(bucketName);  
            
            System.out.println("basic initial MPU");
            InitiateMultipartUploadResult initRequest1 = s3.initiateMultipartUpload(config1);     
            InitiateMultipartUploadResult initRequest2 = s3.initiateMultipartUpload(config2);
            InitiateMultipartUploadResult initRequest3 = s3.initiateMultipartUpload(config3);   
            InitiateMultipartUploadResult initRequest4 = s3.initiateMultipartUpload(config4);   
            InitiateMultipartUploadResult initRequest5 = s3.initiateMultipartUpload(config5);   
			String UploadID1 = initRequest1.getUploadId();
			String UploadID2 = initRequest2.getUploadId();
			String UploadID3 = initRequest3.getUploadId();
			String UploadID4 = initRequest4.getUploadId();
			String UploadID5 = initRequest5.getUploadId();
						
			count = ListMPUs(bucketName, UploadID1); 
			
			System.out.println("Tear down..");
			AbortMultipartUploadRequest abort1 = new AbortMultipartUploadRequest(bucketName,fileName1,UploadID1);
			AbortMultipartUploadRequest abort2 = new AbortMultipartUploadRequest(bucketName,fileName2,UploadID2);
			AbortMultipartUploadRequest abort3 = new AbortMultipartUploadRequest(bucketName,fileName3,UploadID3);
			AbortMultipartUploadRequest abort4 = new AbortMultipartUploadRequest(bucketName,fileName4,UploadID4);
			AbortMultipartUploadRequest abort5 = new AbortMultipartUploadRequest(bucketName,fileName5,UploadID5);
			s3.abortMultipartUpload(abort1);
			s3.abortMultipartUpload(abort2);
			s3.abortMultipartUpload(abort3);
			s3.abortMultipartUpload(abort4);
			s3.abortMultipartUpload(abort5);			
			s3.deleteObject(bucketName, fileName1);
			s3.deleteObject(bucketName, fileName2);
			s3.deleteObject(bucketName, fileName3);
			s3.deleteObject(bucketName, fileName4);
			s3.deleteObject(bucketName, fileName5);
	        s3.deleteBucket(bucketName);
	        System.out.println("DONE");

            
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
	
	public static void main(String args[]) throws IOException, Exception
	{
    	System.out.println("hello java");
    	//ListMPUs();
    	//String old = "Thu Jan 03 13:16:06 GMT+08:00 2013"; //NG
    	//String old= "2004-05-03T17:30:08Z"; //OK
    	//String old= "2004-05-03T17:30:08000Z"; //ok
    	String old= "2004-05-03T17:30:08Z";
    	
    	try
    	{
    		DateUtils dateUtils= new DateUtils();
			Date times = dateUtils.parseIso8601Date(old);
    		System.out.println(times);
    	}
    	catch (ParseException e) 
    	{
    		throw new RuntimeException("Non-ISO8601 date for LastModified in copy object output: "+ old, e);
    	}
    	
    /*	String old = "2013-01-03 14:40:02.0";
    	String month="";
    	String day="";
    	String year="";
    	String time="";
    	
    	old = old.replace("GMT+08:00", "-");
    	System.out.println(old);
    	
    	Pattern pat;
    	Matcher mat;
    	Boolean found;
    	
    	pat = Pattern.compile("[-][0-9][0-9][-][0-9][0-9]");
    	mat = pat.matcher(old);
    	found=mat.find();
    	System.out.println(found);
    	System.out.println(mat.group(0));
    	if(found==true)
    	{
    		String temp = mat.group();
    		month = temp.substring(1, 3);
    		day = temp.substring(4, 6);
    		
    	}*/
   /* 	
    	pat = Pattern.compile("[a-zA-Z][a-z][a-z]");
    	mat = pat.matcher(old);
    	found=mat.find();
    	if(found==true)
    	{
    		month = mat.group();
    		if(month.equalsIgnoreCase("Jan")){month="01";}
        	else if(month.equalsIgnoreCase("Feb")){month="02";}
        	else if(month.equalsIgnoreCase("Mar")){month="03";}
        	else if(month.equalsIgnoreCase("Apr")){month="04";}
        	else if(month.equalsIgnoreCase("May")){month="05";}
        	else if(month.equalsIgnoreCase("Jun")){month="06";}
        	else if(month.equalsIgnoreCase("Jul")){month="07";}
        	else if(month.equalsIgnoreCase("Aug")){month="08";}
        	else if(month.equalsIgnoreCase("Sep")){month="09";}
        	else if(month.equalsIgnoreCase("Oct")){month="10";}
        	else if(month.equalsIgnoreCase("Nov")){month="11";}
        	else if(month.equalsIgnoreCase("Dec")){month="12";}
    	}*/
    	
 /*   	pat = Pattern.compile("[0-9][0-9][0-9][0-9]");
    	mat = pat.matcher(old);
    	found=mat.find();
    	if(found==true)
    	{
    		year = mat.group();
    	}
    	
    	pat = Pattern.compile("[0-9][0-9][:][0-9][0-9][:][0-9][0-9]");
    	mat = pat.matcher(old);
    	found=mat.find();
    	if(found==true)
    	{
    		time = mat.group();
    	}
    	
       	DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    	Date temp = dateFormat.parse(time);
    	temp.setHours(temp.getHours()-8);
    	time = temp.getHours() + ":" + temp.getMinutes() + ":" + temp.getMinutes();
    	
    	String result = year + '-' + month + '-' + day + 'T' + time + 'Z';
    	System.out.println(result);*/
    	
    /*	try
    	{
    		DateUtils dateUtils= new DateUtils();
			Date times = dateUtils.parseIso8601Date(old);
    		System.out.println(times);
    	}
    	catch (ParseException e) {
    		throw new RuntimeException("Non-ISO8601 date for LastModified in copy object output: "+ old, e);
    		}*/
    	//System.out.println(found);
    	//System.out.println(mat.group());
    	
    	
    	//String text = "2013-01-03T02:07:09Z"; //OK
    	//String text = "2013-01-03T02:07:09.000Z"; //OK
    	//String text = "2013-01-03T11:26:09Z"; 
    	
 /*   	
    	
    	String month=old.substring(4, 7);
    	if(month.equalsIgnoreCase("Jan")){month="01";}
    	else if(month.equalsIgnoreCase("Feb")){month="02";}
    	else if(month.equalsIgnoreCase("Mar")){month="03";}
    	else if(month.equalsIgnoreCase("Apr")){month="04";}
    	else if(month.equalsIgnoreCase("May")){month="05";}
    	else if(month.equalsIgnoreCase("Jun")){month="06";}
    	else if(month.equalsIgnoreCase("Jul")){month="07";}
    	else if(month.equalsIgnoreCase("Aug")){month="08";}
    	else if(month.equalsIgnoreCase("Sep")){month="09";}
    	else if(month.equalsIgnoreCase("Oct")){month="10";}
    	else if(month.equalsIgnoreCase("Nov")){month="11";}
    	else if(month.equalsIgnoreCase("Dec")){month="12";}
    	//else{month="null";}

    	String day=old.substring(8, 10);
    	String time=old.substring(11, 19);
    	String year=old.substring(30, 34);
    	
    	DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    	Date temp = dateFormat.parse(time);
    	temp.setHours(temp.getHours()-8);
    	time = temp.getHours() + ":" + temp.getMinutes() + ":" + temp.getMinutes();
    	
    //	System.out.println(temp);
    	
    	
    	//time = hour.v
    	
    	String result = year + '-' + month + '-' + day + 'T' + time + 'Z';
    	System.out.println(result);
    	
    	
    	try
    	{
    		DateUtils dateUtils= new DateUtils();
			Date times = dateUtils.parseIso8601Date(result);
    		System.out.println(times);
    	}
    	catch (ParseException e) {
    		throw new RuntimeException("Non-ISO8601 date for LastModified in copy object output: "+ result, e);
    		}*/
    	

	}
		
}