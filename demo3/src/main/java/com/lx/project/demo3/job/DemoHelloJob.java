package com.lx.project.demo3.job;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DemoHelloJob implements BaseJob {
    public DemoHelloJob(){

    }
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException{
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String msg = jobDataMap.getString("myJob");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        System.out.println("helloJob 执行时间：" + simpleDateFormat.format(new Date()) + "附带参数："+ msg);
    }
}
