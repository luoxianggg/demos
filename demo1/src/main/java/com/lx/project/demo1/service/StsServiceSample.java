package com.lx.project.demo1.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.lx.project.demo1.model.Response;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StsServiceSample {
    String endpoint = "sts.cn-shanghai.aliyuncs.com";
    String accessKeyId = "LTAIDnfiOc3RQq9Y";
    String accessKeySecret = "cMiR0achBbAQ4377pAluyAzkASjA7e";
    String roleArn = "acs:ram::1010552741727341:role/aliyunosstokengeneratorrole";
    String roleSessionName = "session-name";
    String policy = "{\n" +
            "    \"Version\": \"1\", \n" +
            "    \"Statement\": [\n" +
            "        {\n" +
            "            \"Action\": [\n" +
            "                \"oss:*\"\n" +
            "            ], \n" +
            "            \"Resource\": [\n" +
            "                \"acs:oss:*:*:*\" \n" +
            "            ], \n" +
            "            \"Effect\": \"Allow\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";
    public  Map  getSTSToken(){
        Response  respons =  new Response();
        Map map = new HashMap();
        try {
            // 添加endpoint（直接使用STS endpoint，前两个参数留空，无需添加region ID）
            DefaultProfile.addEndpoint("", "", "Sts", endpoint);
            // 构造default profile（参数留空，无需添加region ID）
            IClientProfile profile = DefaultProfile.getProfile("", accessKeyId, accessKeySecret);
            // 用profile构造client
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setMethod(MethodType.POST);
            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy); // Optional
            final AssumeRoleResponse response = client.getAcsResponse(request);
            System.out.println("Expiration: " + response.getCredentials().getExpiration());
            System.out.println("Access Key Id: " + response.getCredentials().getAccessKeyId());
            System.out.println("Access Key Secret: " + response.getCredentials().getAccessKeySecret());
            System.out.println("Security Token: " + response.getCredentials().getSecurityToken());
            System.out.println("RequestId: " + response.getRequestId());
            map.put("Success" , "true");
            map.put("Expiration" , response.getCredentials().getExpiration());
            map.put("Access Key Id",response.getCredentials().getAccessKeyId());
            map.put("Access Key Secret",response.getCredentials().getAccessKeySecret());
            map.put("Security Token",response.getCredentials().getSecurityToken());
            map.put("RequestId",response.getRequestId());

        } catch (ClientException e) {
            System.out.println("Failed：");
            System.out.println("Error code: " + e.getErrCode());
            System.out.println("Error message: " + e.getErrMsg());
            System.out.println("RequestId: " + e.getRequestId());
            map.put("Success" , "false");
            map.put("ErrorCode",e.getErrCode());
            map.put("ErrorMessage",e.getErrMsg());
            map.put("ErrorMessage",e.getErrMsg());
            map.put("RequestId",e.getRequestId());
        }
        return map;
    }
}
