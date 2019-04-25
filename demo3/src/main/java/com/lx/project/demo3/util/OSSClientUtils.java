package com.lx.project.demo3.util;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.lx.project.demo3.bean.OSSClientConstans;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class OSSClientUtils {



    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-m-dd");
    private static String endpoint= OSSClientConstans.JAVA4ALL_END_POINT;
    private static String accessKeyId=OSSClientConstans.JAVA4ALL_ACCESS_KEY_ID;
    private static String accessKeySecret=OSSClientConstans.JAVA4ALL_ACCESS_KEY_SECRET;
    private static String bucketName=OSSClientConstans.JAVA4ALL_BUCKET_NAME1;
    private static String fileHost=OSSClientConstans.JAVA4ALL_FILE_HOST;


   public static void uploadHelloWorld(){
       // 创建OSSClient实例。
       OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

          // 上传字符串。
       String content = "Hello OSS";
       ossClient.putObject(bucketName, "hellowOSS", new ByteArrayInputStream(content.getBytes()));

          // 关闭OSSClient。
       ossClient.shutdown();
   }
    public static void getObject(){
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        ossClient.getObject(new GetObjectRequest(bucketName, "hellowOSS"), new File("D:/upload/helloOSS.txt"));

        // 关闭OSSClient。
        ossClient.shutdown();
    }
    /*
    * 上传文件
    * luoxiang
    * 2019年3月12日20:42:08
    *
    * */
    public static String upload(String fileName){

            File file = new File(fileName);
            System.out.println("=========>OSS文件上传开始："+file.getName());

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = format.format(new Date());
            if(null == file){
                return null;
            }

            OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
            try {
                //容器不存在，就创建
                if(! ossClient.doesBucketExist(bucketName)){
                    ossClient.createBucket(bucketName);
                    CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                    createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                    ossClient.createBucket(createBucketRequest);
                }
                //创建文件路径
                String fileUrl = fileHost+"/"+(dateStr + "/" + UUID.randomUUID().toString().replace("-","")+"-"+file.getName());
                //上传文件
                PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, file));
                //设置权限 这里是公开读
                ossClient.setBucketAcl(bucketName,CannedAccessControlList.Private);
                if(null != result){
                    System.out.println("==========>OSS文件上传成功,OSS地址："+fileUrl);
                    return fileUrl;
                }
            }catch (OSSException oe){
                System.out.println(oe.getMessage());
            }catch (ClientException ce){
                System.out.println(ce.getMessage());
            }finally {
                //关闭
                ossClient.shutdown();
            }
            return null;
        }
    /*
    *
    * 获取文件名列表
    * luoxiang
    * 2019年3月12日20:29:05
    *
    *
    * */

    public static List<String> getObjectList(String bucketName){
        List<String> listBe = new ArrayList<>();
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        try{
            System.out.println("=========>查询文件名列表");

            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
            listObjectsRequest.setPrefix("blog/"+format.format(new Date()) + "/");
            ObjectListing list = ossClient.listObjects(listObjectsRequest);
            for(OSSObjectSummary ossObjectSummary : list.getObjectSummaries()){
                listBe.add(ossObjectSummary.getKey());
            }
            ossClient.shutdown();
        }catch (Exception e){
            System.out.println("================查询列表失败"+e);
            ossClient.shutdown();
            return new ArrayList<>();
        }
        return listBe;
    }

    /*
    *
    * 文件断点续传
    * luoxiang
    * 2019年3月12日20:44:27
    *
    * */

    public static String videoBreakpointUpload(String filePath){

        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        try {
        UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, accessKeyId);

        // The local file to upload---it must exist.

        uploadFileRequest.setUploadFile(filePath);

        // Sets the concurrent upload task number to 5.

        uploadFileRequest.setTaskNum(5);

        // Sets the part size to 1MB.

        uploadFileRequest.setPartSize(1024 * 1024 * 1);

        // Enables the checkpoint file. By default it's off.

        uploadFileRequest.setEnableCheckpoint(true);

        UploadFileResult uploadResult = ossClient.uploadFile(uploadFileRequest);

        CompleteMultipartUploadResult multipartUploadResult =

                uploadResult.getMultipartUploadResult();


            return multipartUploadResult.getETag();

        }catch (Throwable throwable) {
            System.out.println(throwable.getMessage());
            return null;
        }finally {
            //关闭
            ossClient.shutdown();
        }

    }

   public static String fileStreamDownload(){
       // 创建OSSClient实例。
       OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

     // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
       OSSObject ossObject = ossClient.getObject(bucketName, "");

       // 读取文件内容。
       System.out.println("Object content:");
       BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
       try {
       while (true) {

           String line = null;

               line = reader.readLine();

           if (line == null) break;

           System.out.println("\n" + line);
       }
        // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
       reader.close();
       } catch (IOException e) {
           e.printStackTrace();
       }
      // 关闭OSSClient。
       ossClient.shutdown();

        return "";
   }

}

