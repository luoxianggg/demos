package com.lx.project.demo1.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import com.lx.project.demo1.bean.Constants;
import com.lx.project.demo1.dao.TestMapper;
import com.lx.project.demo1.model.order.Order;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RabbitOrderSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    TestMapper testMapper;


    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override

        public void confirm(@Nullable CorrelationData correlationData, boolean b, @Nullable String s) {
            System.err.println("correlationData:" + correlationData);
            String messageId = correlationData.getId();
            if (b) {
                //返回成功则更新消息记录
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("message_id", messageId);
                map.put("status", Constants.ORDER_SEND_SUCCESS);
                testMapper.changeBrokerMessageLogStatus(map);
            } else {
                System.err.println("异常处理");
            }
        }
    };
 //发送消息方法调用
    public void senderOrder(Order order) throws Exception{
        rabbitTemplate.setConfirmCallback(confirmCallback);
        CorrelationData correlationData = new CorrelationData(order.getMessageId());
        String senderMsg = JSON.toJSONString(order);
    //    rabbitTemplate.setMessageConverter(integrationEventMessageConverter());
        rabbitTemplate.convertAndSend("order-exchange", "order.ABC", senderMsg, correlationData);

    }
    public MessageConverter integrationEventMessageConverter() {
        FastJsonHttpMessageConverter4 messageConverter = new FastJsonHttpMessageConverter4();
        return (MessageConverter)messageConverter;
    }

}
