import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.UUID;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * ���d�ҵ{�����ܽd�p��ϥ�hicloud S3 JAVA SDK�o�e�򥻪�request��hicloud S3
 * 
 * �e���G���������ohicloud S3�}�o�һݤ�AccessKey�PSecretKey
 * ���oAccessKey�PSecretKey���y�{�A�аѦ�"hicloud S3 Quick Start"��� : 
 * https://userportal.hicloud.hinet.net/cloud/document/files/hicloud-S3-QuickStart.pdf 
 *
 * ���n�G�b�B�榹�d�ҵ{���e�A�аȥ��T�{�w�Nhicloud S3 access credentials��JAwsCredentials.properties�ɮפ�                  
 */
public class hicloud_S3Sample {

    public static void main(String[] args) throws IOException {
        /*
         * ���n�G�b�B�榹�d�ҵ{���e�A�аȥ��T�{�w�Nhicloud S3 access credentials��JAwsCredentials.properties�ɮפ�
         */
        AmazonS3 s3 = new AmazonS3Client(new PropertiesCredentials(hicloud_S3Sample.class.getResourceAsStream("AwsCredentials.properties")));

        String bucketName = "my-first-s3-bucket";
        String key = "MyObjectKey";

        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon S3");
        System.out.println("===========================================\n");

        try {
            /*
             * �Ы�Bucket - Bucket �W�٥����O�ߤ@�A�Y Bucket �W�٤w�Q��L�ϥΪ̩ҨϥήɡA�N�L�k���\�إ߬ۦP�W�٪�Bucket
             */
            System.out.println("Creating bucket " + bucketName + "\n");
            s3.createBucket(bucketName);

            /*
             * �C�X�b���U�Ҧ���Bucket
             */
            System.out.println("Listing buckets");
            for (Bucket bucket : s3.listBuckets()) {
                System.out.println(" - " + bucket.getName());
            }
            System.out.println();

            /*
             * �W��Object��ҫإߪ�Bucket - �i�ĥ�file�榡��InputStream�榡�ӤW���ɮ�
             * �b�W���ɮת��P�ɡA�]����]�w�ӤH��metadata�A�pcontent-type�Bcontent-encoding��metadata
             */
            System.out.println("Uploading a new object to S3 from a file\n");
            s3.putObject(new PutObjectRequest(bucketName, key, createSampleFile()));

            /*
             * �U��Object - ��U��Object�ɡA�|�NObject����metadata��Object���e���U���^��
             *
             * GetObjectRequest�]���ѱ���U�����ﶵ�A�p�i���Object�ק�ɶ��b�Y�ɶ��᪺Object�U���A�ο�ܯS�wETags��Object�U���A�Υu�U������Object
             */
            System.out.println("Downloading an object");
            S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
            System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
            displayTextInputStream(object.getObjectContent());

            /*
             * �C�XBucket���Ҧ�Prefix��"My"��Object
             */
            System.out.println("Listing objects");
            ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
                    .withBucketName(bucketName)
                    .withPrefix("My"));
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                System.out.println(" - " + objectSummary.getKey() + "  " +
                                   "(size = " + objectSummary.getSize() + ")");
            }
            System.out.println();

            /*
             * �R��Object - ���D�bVersioning����}�Ҫ����p�U�A����R��Object���ʧ@���O�L�k�^�_���A�]�������ԷV����ܱ��R����Object
             */
            System.out.println("Deleting an object\n");
            s3.deleteObject(bucketName, key);

            /*
             * �R��Bucket - ���R��Bucket�A��Bucket�����O�w�M�Ū��A�]�����R��Bucket�e�Х��T�{Bucket���O�_���s�b����Object
             */
            System.out.println("Deleting bucket " + bucketName + "\n");
            s3.deleteBucket(bucketName);
        } catch (AmazonServiceException ase) {
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

    /**
     * �إ߼Ȧs�ɡA�Ω�d�Ҥ��W�Ǧ�hicloud S3
     *
     * @return �^�Ǥ@ File Object�A�N��Ȯɫإߪ���r��
     * @throws IOException
     */
    private static File createSampleFile() throws IOException {
        File file = File.createTempFile("aws-java-sdk-", ".txt");
        file.deleteOnExit();

        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write("abcdefghijklmnopqrstuvwxyz\n");
        writer.write("01234567890112345678901234\n");
        writer.write("!@#$%^&*()-=[]{};':',.<>/?\n");
        writer.write("01234567890112345678901234\n");
        writer.write("abcdefghijklmnopqrstuvwxyz\n");
        writer.close();

        return file;
    }

    /**
     * �L�Xinput stream�����e
     *
     * @param input ����ܪ�input stream
     * @throws IOException
     */
    private static void displayTextInputStream(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null) break;

            System.out.println("    " + line);
        }
        System.out.println();
    }

}
