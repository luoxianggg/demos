package com.lx.project.demo3.service;

import java.util.HashMap;
import java.util.Map;

import com.lx.project.demo3.model.Response;

public interface UmsMemberService {
public Response generateAuthCode(HashMap<String,Object> map);
public Response verifyAuthCode(HashMap<String,Object> map);

}
