package com.lx.project.demo3.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewJob implements BaseJob{
    public NewJob(){

    }
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        System.out.println("helloJob 执行时间：" + simpleDateFormat.format(new Date()));
    }
}
