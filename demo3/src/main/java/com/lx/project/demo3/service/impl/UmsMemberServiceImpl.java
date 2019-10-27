package com.lx.project.demo3.service.impl;

import java.util.HashMap;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.lx.project.demo3.model.Response;
import com.lx.project.demo3.service.RedisService;
import com.lx.project.demo3.service.UmsMemberService;

import io.micrometer.core.instrument.util.StringUtils;

public class UmsMemberServiceImpl implements UmsMemberService{
	@Autowired
	private RedisService redisService;
	
	@Value("${redis.key.prefix.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    @Value("${redis.key.expire.authCode}")
    private Long AUTH_CODE_EXPIRE_SECONDS;
    
	public Response generateAuthCode(HashMap<String,Object> map) {
		Response response = new Response();
		StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        //验证码绑定手机号并存储到redis
        redisService.set(REDIS_KEY_PREFIX_AUTH_CODE + map.get("telephone"), sb.toString());
        redisService.expire(REDIS_KEY_PREFIX_AUTH_CODE + map.get("telephone"), AUTH_CODE_EXPIRE_SECONDS);
		return response.success(sb.toString());
	}

	//对输入的验证码进行校验
    @Override
    public Response verifyAuthCode(HashMap<String,Object> map) {
    	Response response = new Response();
    	String authCode =  map.get("authCode").toString();
    	String telephone =  map.get("telephone").toString();
    	if (StringUtils.isEmpty(authCode)) {
            return response.failure("请输入验证码");
        }
        String realAuthCode = redisService.get(REDIS_KEY_PREFIX_AUTH_CODE + telephone);
        boolean result = authCode.equals(realAuthCode);
        if (result) {
            return response.success("验证码校验成功");
        }
       return response.failure("验证码不正确");
    }
}
