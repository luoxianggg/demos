package com.lx.project.demo1.filter;

import com.alibaba.fastjson.JSON;
import com.lx.project.demo1.model.PerformeanceMonitor;
import com.lx.project.demo1.service.LogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Aspect
public class LogFilter{

    @Autowired
    HttpServletRequest request;

    @Autowired
    LogService logService;

    /*
    * 监听系统所有前端请求
    *
    * */
    @Pointcut("execution(* com.lx.project.demo1.controller..*.*(..)))")
    public  void controllerCall(){

    }
    /*
    * 记录日志
    * */
    @Before(value = "controllerCall()")
    public  void beforeCallInfo(JoinPoint call){
        PerformeanceMonitor.begin();
    }
    @AfterReturning(value = "controllerCall()",returning = "res")
    public void updateCallInfo(JoinPoint joinPoint, Map<String,Object> res){

        Map<String,Object> processdData = new HashMap<>();
        LinkedHashMap<String,Object> args = null;
        Object[] objectList = joinPoint.getArgs();
        if(objectList != null){
            if(objectList.length > 1){
                args = (LinkedHashMap<String,Object>) objectList[1];
            }
        }


        Long cost = PerformeanceMonitor.end();
        processdData.put("postData",args == null ? null:args.toString());
        processdData.put("responseData", JSON.toJSONString(res));
        processdData.put("costTime",cost);
        logService.log(processdData);
        System.out.println(processdData.toString());

    }

}
