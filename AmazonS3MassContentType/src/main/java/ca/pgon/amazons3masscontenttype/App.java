/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.amazons3masscontenttype;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class App {

    // Parameters
    private static String awsAccessKeyId;
    private static String awsSecretAccessKey;
    private static String bucketName;
    private static String contentType;

    private static AmazonS3Client amazonS3Client;

    private static int count = 0;

    public static void main(String[] args) {

        if (args.length != 4) {
            System.out.println("Usage: AWS_ACCESS_KEY_ID AWS_SECRET_ACCESS_KEY BUCKET_NAME CONTENT_TYPE");
            return;
        }

        // Get the parameters
        int i = 0;
        awsAccessKeyId = args[i++];
        awsSecretAccessKey = args[i++];
        bucketName = args[i++];
        contentType = args[i++];

        // Prepare service
        AWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);
        amazonS3Client = new AmazonS3Client(awsCredentials);

        // Go through the first page
        ObjectListing objectListing = amazonS3Client.listObjects(bucketName);
        process(objectListing);
        // Go through the other pages
        while (objectListing.isTruncated()) {
            amazonS3Client.listNextBatchOfObjects(objectListing);
            process(objectListing);
        }

        System.out.println();
        System.out.println("Processed " + count + " files");
    }

    private static void process(ObjectListing objectListing) {
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            // Show the key
            String key = objectSummary.getKey();
            System.out.println(key);

            // Get the metadata and check the content type
            ObjectMetadata objectMetadata = amazonS3Client.getObjectMetadata(bucketName, key);
            System.out.println("\tCurrent content type: " + objectMetadata.getContentType());
            if (!contentType.equals(objectMetadata.getContentType())) {
                System.out.println("\tChanging content type for : " + contentType);
                objectMetadata.setContentType(contentType);

                // Get the current ACL
                AccessControlList accessControlList = amazonS3Client.getObjectAcl(bucketName, key);

                // Modify the file
                CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucketName, key, bucketName, key);
                copyObjectRequest.withNewObjectMetadata(objectMetadata);
                copyObjectRequest.withAccessControlList(accessControlList);
                amazonS3Client.copyObject(copyObjectRequest);
            }
            ++count;
        }
    }
}
