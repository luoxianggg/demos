package com.lx.project.demo3.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.lx.project.demo3.bean.OSSClientConstans;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/*
*
* 分片上传工具类
* luoxiang
* 2019年3月13日22:08:47
*
* */
public class OSSClientDisPartUploadUtil {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(OSSClientUtils.class);
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-m-dd");
    private static String endpoint= OSSClientConstans.JAVA4ALL_END_POINT;
    private static String accessKeyId=OSSClientConstans.JAVA4ALL_ACCESS_KEY_ID;
    private static String accessKeySecret=OSSClientConstans.JAVA4ALL_ACCESS_KEY_SECRET;
    private static String bucketName=OSSClientConstans.JAVA4ALL_BUCKET_NAME1;
    private static String fileHost=OSSClientConstans.JAVA4ALL_FILE_HOST;
    private static List<PartETag> partETags = Collections.synchronizedList(new ArrayList<PartETag>());
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    private static OSSClient ossClient  = new OSSClient(endpoint,accessKeyId,accessKeySecret);


    /*
   * 文件分批分片上传
   * luoxiang
   * 2019年3月13日20:20:00
   *
   * */
        public static String uploadFile(String localFilePath)throws IOException {

            String uploadId = claimUploadId();
            System.out.println("Claiming a new upload id " + uploadId + "\n");
            try {
            /*
             * Claim a upload id firstly
             */


            /*
             * Calculate how many parts to be divided
             */
                final long partSize = 5 * 1024 * 1024L;   // 5MB
                final File sampleFile =  new File(localFilePath);//createSampleFile();
                long fileLength = sampleFile.length();
                int partCount = (int) (fileLength / partSize);
                if (fileLength % partSize != 0) {
                    partCount++;
                }
                if (partCount > 10000) {
                    throw new RuntimeException("Total parts count should not exceed 10000");
                } else {
                    System.out.println("Total parts count " + partCount + "\n");
                }
            /*
             * Upload multiparts to your bucket
             */
                System.out.println("Begin to upload multiparts to OSS from a file\n");
                for (int i = 0; i < partCount; i++) {
                    long startPos = i * partSize;
                    long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
                    executorService.execute(new PartUploader(sampleFile, startPos, curPartSize, i + 1, uploadId));
                }

            /*
             * Waiting for all parts finished
             */
                executorService.shutdown();
                while (!executorService.isTerminated()) {
                    try {
                        executorService.awaitTermination(5, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            /*
             * Verify whether all parts are finished
             */
                if (partETags.size() != partCount) {

                    throw new IllegalStateException("Upload multiparts fail due to some parts are not finished yet");
                } else {
                    System.out.println("Succeed to complete multiparts into an object named " + accessKeyId + "\n");
                }

            /*
             * View all parts uploaded recently
             */
                listAllParts(uploadId);

            /*
             * Complete to upload multiparts
             */
                completeMultipartUpload(uploadId);

            /*

             * Fetch the object that newly created at the step below.

             */

             //   System.out.println("Fetching an object");

             //   ossClient.getObject(new GetObjectRequest(bucketName, accessKeyId), new File(localFilePath));



            } catch (OSSException oe) {

                System.out.println("Caught an OSSException, which means your request made it to OSS, "

                        + "but was rejected with an error response for some reason.");

                System.out.println("Error Message: " + oe.getErrorMessage());

                System.out.println("Error Code:       " + oe.getErrorCode());

                System.out.println("Request ID:      " + oe.getRequestId());

                System.out.println("Host ID:           " + oe.getHostId());

            } catch (ClientException ce) {

                System.out.println("Caught an ClientException, which means the client encountered "

                        + "a serious internal problem while trying to communicate with OSS, "

                        + "such as not being able to access the network.");

                System.out.println("Error Message: " + ce.getMessage());

            } finally {

            /*

             * Do not forget to shut down the client finally to release all allocated resources.

             */
                if (ossClient != null) {

                    ossClient.shutdown();

                }

            }
            return uploadId;
        }

    private static class PartUploader implements Runnable {
            private File localFile;

            private long startPos;
            private long partSize;
            private int partNumber;
            private String uploadId;

            public PartUploader(File localFile, long startPos, long partSize, int partNumber, String uploadId) {
                this.localFile = localFile;
                this.startPos = startPos;
                this.partSize = partSize;
                this.partNumber = partNumber;
                this.uploadId = uploadId;
            }


            @Override
            public void run() {
                InputStream instream = null;
                try {
                    instream = new FileInputStream(this.localFile);
                    instream.skip(this.startPos);
                    UploadPartRequest uploadPartRequest = new UploadPartRequest();
                    uploadPartRequest.setBucketName(bucketName);
                    uploadPartRequest.setKey(accessKeyId);
                    uploadPartRequest.setUploadId(this.uploadId);
                    uploadPartRequest.setInputStream(instream);
                    uploadPartRequest.setPartSize(this.partSize);
                    uploadPartRequest.setPartNumber(this.partNumber);
                    UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                    System.out.println("Part#" + this.partNumber + " done\n");
                    synchronized (partETags) {
                        partETags.add(uploadPartResult.getPartETag());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (instream != null) {
                        try {
                            instream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }

            }

        }

    private static File createSampleFile() throws IOException {
        File file = File.createTempFile("oss-java-sdk-", ".txt");
        file.deleteOnExit();

        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        for (int i = 0; i < 1000000; i++) {
            writer.write("abcdefghijklmnopqrstuvwxyz\n");
            writer.write("0123456789011234567890\n");
        }
        writer.close();
        return file;
    }
    private static String claimUploadId() {

        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, accessKeyId);

        InitiateMultipartUploadResult result =  ossClient.initiateMultipartUpload(request);

        return result.getUploadId();

    }



    private static void completeMultipartUpload(String uploadId) {

        // Make part numbers in ascending order

        Collections.sort(partETags, new Comparator<PartETag>() {



            @Override

            public int compare(PartETag p1, PartETag p2) {

                return p1.getPartNumber() - p2.getPartNumber();

            }

        });



        System.out.println("Completing to upload multiparts\n");

        CompleteMultipartUploadRequest completeMultipartUploadRequest =

                new CompleteMultipartUploadRequest(bucketName, accessKeyId, uploadId, partETags);

        ossClient.completeMultipartUpload(completeMultipartUploadRequest);

    }



    private static void listAllParts(String uploadId) {

        System.out.println("Listing all parts......");

        ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, accessKeyId, uploadId);

        PartListing partListing = ossClient.listParts(listPartsRequest);



        int partCount = partListing.getParts().size();

        for (int i = 0; i < partCount; i++) {

            PartSummary partSummary = partListing.getParts().get(i);

            System.out.println("\tPart#" + partSummary.getPartNumber() + ", ETag=" + partSummary.getETag());

        }

        System.out.println();

    }
}
