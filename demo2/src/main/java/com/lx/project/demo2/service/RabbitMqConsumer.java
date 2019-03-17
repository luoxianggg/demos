package com.lx.project.demo2.service;

import com.alibaba.fastjson.JSONObject;
import com.lx.project.demo2.model.Order;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RabbitMqConsumer {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "order-queue",durable = "true"),
                    exchange = @Exchange(name = "order-exchange",durable = "true",type = "topic"),
                    key = "order.*"
            )
    )

    @RabbitHandler
    public void onOrderMessage(@Payload String order,
                               @Headers Map<String,Object> headers,
                               Channel channel)throws Exception{
        Order order1 = JSONObject.parseObject(order,Order.class);
        System.out.println("-------收到消息，开始消费------");
        System.out.println("订单id："+ order1.getId());

        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag,false);
    }

}
