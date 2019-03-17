package com.lx.project.demo1.dao;

import com.lx.project.demo1.model.JobAndTrigger;
import com.lx.project.demo1.model.order.BrokerMessageLog;
import com.lx.project.demo1.model.order.Order;

import java.util.List;
import java.util.Map;

public interface TestMapper {

    /**
     * 查询消息状态为0(发送中) 且已经超时的消息集合
     * @return
     */
    List<BrokerMessageLog> query4StatusAndTimeoutMessage(Map<String,Object> map);

    /**
     * 重新发送统计count发送次数 +1

     */
    void update4ReSend(Map<String,Object> map);
    /**
     * 更新最终消息发送结果 成功 or 失败
     */
    void changeBrokerMessageLogStatus(Map<String,Object> map);
    /*
    * 插入记录
    * */
    void insertBrokerMessageLog(Map<String,Object> map);

    /*
* 订单插入
* */
    public void insertOrder(Map<String, Object> map);

    /*
    *
    * d订单查询
    * */
    public List<Order> queryOrderById(Map<String, Object> map);

    /*
    * 订单名修改
    * */
    public void update_order_name(Map<String, Object> map);

    public  List<JobAndTrigger> getJobAndTriggerDetails(Map<String,Object> map);

}
