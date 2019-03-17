package com.lx.project.demo3.service.impl;

import com.lx.project.demo3.service.LogService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LogServiceImpl implements LogService {
    @Override
    public  void log(Map<String,Object> map){
        //sysBaseInfo.insertLog(map);
    }
}
