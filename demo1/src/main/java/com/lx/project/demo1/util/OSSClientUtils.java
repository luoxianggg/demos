package com.lx.project.demo1.util;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.lx.project.demo1.bean.OSSClientConstans;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;


public class OSSClientUtils {



    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-m-dd");
    private static String endpoint= OSSClientConstans.JAVA4ALL_END_POINT;
    private static String accessKeyId=OSSClientConstans.JAVA4ALL_ACCESS_KEY_ID;
    private static String accessKeySecret=OSSClientConstans.JAVA4ALL_ACCESS_KEY_SECRET;
    private static String bucketName=OSSClientConstans.JAVA4ALL_BUCKET_NAME1;
    private static String fileHost=OSSClientConstans.JAVA4ALL_FILE_HOST;

    /*
    * 上传文件
    * luoxiang
    * 2019年3月12日20:42:08
    *
    * */
    public static String upload(File file){

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
                ossClient.setBucketAcl(bucketName,CannedAccessControlList.PublicRead);
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
        ObjectMetadata metadata = new ObjectMetadata();
        //指定上传内容
        metadata.setContentType("text/pain");
        //通过UploadFileRequest设置多个参数
        UploadFileRequest uploadFileRequest = new UploadFileRequest(OSSClientConstans.JAVA4ALL_BUCKET_NAME1,format.format(new Date()));

        //指定本地上传文件
         uploadFileRequest.setUploadFile(filePath);
         //指定并发线程数，默认为1
        uploadFileRequest.setTaskNum(5);
        //指定上传分片大小
        uploadFileRequest.setPartSize(1*1024*1024);
        //记录本地分片上传结果的文件，开启断点续传时需要设置此参数，上传过程中的进度信息会保存在该文件中，
        //如果某一分片上传失败，再次上传时会根据文件记录的点续传，上传完成后该文件会被删除。
        //默认与待上传的本地文件通目录。为uploadFile.ucp
        uploadFileRequest.setEnableCheckpoint(true);
        uploadFileRequest.setCheckpointFile("E:/OSSUploadFileLog.txt");
        //设置元数据
        uploadFileRequest.setObjectMetadata(metadata);
        //设置上传成功回调

        try {


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

}

