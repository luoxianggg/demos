package com.lx.project.demo1.service.impl;

import com.github.pagehelper.PageInfo;
import com.lx.project.demo1.dao.TestMapper;
import com.lx.project.demo1.job.BaseJob;
import com.lx.project.demo1.model.JobAndTrigger;
import com.lx.project.demo1.service.SchehulerService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SchedulerServiceImpl implements SchehulerService {
    @Autowired @Qualifier("Scheduler")
    Scheduler scheduler;
    @Autowired
    TestMapper testMapper;

    @Override
    public void addJobs(Map<String, Object> map)throws Exception {
        scheduler.start();
        String JobClassName = map.get("jobClassName").toString();
        String JobGropeName = map.get("jobGropeName").toString();
        String CronExpersion = map.get("cronExpresion").toString();
        JobDetail jobDetail = JobBuilder.newJob(getClazz(JobClassName).getClass())
                .withIdentity(JobClassName,JobGropeName)
                .usingJobData("myJob","任务1")
                .build();
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(CronExpersion);

        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(JobClassName,JobGropeName)
                .withSchedule(scheduleBuilder).build();
        try{
            scheduler.scheduleJob(jobDetail,trigger);
        }catch (SchedulerException e){
            System.out.println("创建定时任务失败"+ e);
            throw new Exception("创建定时任务失败");
        }
    }

    @Override
    public void pauseJob(Map<String, Object> map) throws SchedulerException{

        String jobClassName = map.get("jobClassName").toString();
        String jobGropeName = map.get("jobGropeName").toString();
        scheduler.pauseJob(JobKey.jobKey(jobClassName,jobGropeName));
    }

    @Override
    public void resumeJob(Map<String, Object> map) throws SchedulerException{
        String jobClassName = map.get("jobClassName").toString();
        String jobGropeName = map.get("jobGropeName").toString();
        scheduler.resumeJob(JobKey.jobKey(jobClassName,jobGropeName));

    }
    public static BaseJob getClazz(String className)throws Exception{
        Class<?> class1 = Class.forName(className);
        return (BaseJob)class1.newInstance();
    }

    @Override
    public void reSchedulerJob(Map<String, Object> map) throws Exception{

        String jobClassName = map.get("jobClassName").toString();
        String jobGropeName = map.get("jobGropeName").toString();
        String CronExpersion = map.get("cronExpresion").toString();

        try{
            TriggerKey triggerKey = new TriggerKey(jobClassName,jobGropeName);
            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(CronExpersion);
            CronTrigger  trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            //按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            //按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey,trigger);
        }catch (SchedulerException e){

            System.out.println("更新定时任务失败"+e);
            throw new Exception("更新定时任务失败");
        }
    }

    @Override
    public void deleteJob(Map<String, Object> map) throws Exception {
        String jobClassName = map.get("jobClassName").toString();
        String jobGropeName = map.get("jobGropeName").toString();
        scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName,jobGropeName));
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName,jobGropeName));
        scheduler.deleteJob(JobKey.jobKey(jobClassName,jobGropeName));
    }

    @Override
    public Map<String, Object> queryJobs(Map<String, Object> map) throws Exception {
        //PageInfo<JobAndTrigger> pageInfo =
        List<JobAndTrigger> list = testMapper.getJobAndTriggerDetails(map);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("jobAndTrigger",list);
        resultMap.put("totalCount",list.size());
        return resultMap;
    }
}
