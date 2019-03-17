package com.lx.project.demo1.service;

import java.util.Map;

public interface SchehulerService {
    /*
    *
    * 新增一个job任务
    *
    * */
    public void addJobs(Map<String,Object> map) throws Exception;

    /*
    * 暂停job任务
    *
    * */
    public void pauseJob(Map<String,Object> map)throws Exception;

    /*
    * 唤醒job任务
    *
    * */
    public void resumeJob(Map<String,Object> map)throws Exception;

    /*
    * 修改job任务
    *
    * */
    public void reSchedulerJob(Map<String,Object> map)throws Exception;

    /*
    *
    * 删除一个job任务
    * */
    public void deleteJob(Map<String,Object> map) throws Exception;

    /*
    *
    * 查询jobs
    * */

    public Map<String,Object> queryJobs(Map<String ,Object> map)throws Exception;
}
