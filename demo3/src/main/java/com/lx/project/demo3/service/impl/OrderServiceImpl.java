package com.lx.project.demo3.service.impl;

import com.alibaba.fastjson.JSON;
import com.lx.project.demo3.bean.Constants;
import com.lx.project.demo3.dao.DemoMapper;
import com.lx.project.demo3.model.order.Order;
import com.lx.project.demo3.service.OrderService;
import com.lx.project.demo3.service.RabbitOrderSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    DemoMapper demoMapper;
    @Autowired
    RabbitOrderSender rabbitOrderSenderService;

@Override
public List<Order> queryOrders(Map<String,Object> map){
    List<Order> list = demoMapper.queryTest(map);
    return list;
}
    @Override
    public void createOrder(Order order) throws Exception{
        // 使用当前时间当做订单创建时间（为了模拟一下简化）
        Date orderTime = new Date();
        Map<String,Object> orderMap = new HashMap<>();
        Map<String,Object> msgMap = new HashMap<>();
        orderMap.put("id",order.getId());
        orderMap.put("name",order.getName());
        orderMap.put("message_id",order.getMessageId());
        // 插入业务数据
        demoMapper.insertOrder(orderMap);
        // 插入消息记录表数据
        // 消息唯一ID
        msgMap.put("message_id",order.getMessageId());
        // 保存消息整体 转为JSON 格式存储入库
        msgMap.put("message", JSON.toJSONString(order));
        // 设置消息状态为0 表示发送中
        msgMap.put("status","0");
        // 设置消息未确认超时时间窗口为 一分钟
        Calendar  calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, Constants.ORDER_TIMEOUT);
        msgMap.put("next_retry", calendar.getTime());
        msgMap.put("create_time",new Date());
        msgMap.put("update_time",new Date());
        demoMapper.insertBrokerMessageLog(msgMap);
        // 发送消息
        rabbitOrderSenderService.senderOrder(order);

    }

}
