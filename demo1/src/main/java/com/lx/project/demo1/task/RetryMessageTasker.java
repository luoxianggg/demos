package com.lx.project.demo1.task;

import com.alibaba.fastjson.JSONObject;
import com.lx.project.demo1.bean.Constants;
import com.lx.project.demo1.dao.TestMapper;
import com.lx.project.demo1.model.order.BrokerMessageLog;
import com.lx.project.demo1.model.order.Order;
import com.lx.project.demo1.service.RabbitOrderSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RetryMessageTasker {
    @Autowired
    private RabbitOrderSender rabbitOrderSenderService;
    @Autowired
    TestMapper testMapper;

    @Scheduled(initialDelay = 5000,fixedDelay = 10000)
    public void resend(){
        Map<String,Object> mapPara = new HashMap<String,Object>();
        List<BrokerMessageLog> list = testMapper.query4StatusAndTimeoutMessage(mapPara);
        list.forEach(messageLog -> {
            if(messageLog.getTry_count() >= 3){
                //update fail message
                Map<String,Object> msgMap= new HashMap<String,Object>();
                msgMap.put("message_id",messageLog.getMessage_id());
                msgMap.put("status", Constants.ORDER_SEND_FAILURE);
                msgMap.put("update_time", new Date());
                testMapper.changeBrokerMessageLogStatus(msgMap);
            } else {
                // resend
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("message_id",messageLog.getMessage_id());
                map.put("update_time", new Date());
                testMapper.update4ReSend(map);
                Order reSendOrder = (Order)JSONObject.parse (messageLog.getMessage());
                try {
                    rabbitOrderSenderService.senderOrder(reSendOrder);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("-----------异常处理-----------");
                }
            }
        });
    }
}
