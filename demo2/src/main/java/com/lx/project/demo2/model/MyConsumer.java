package com.lx.project.demo2.model;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

//继承DefaultConsumer 的类可自动监听broker端，并在实现的handelDelivery方法里面处理获得信息
public class MyConsumer extends DefaultConsumer {
    private Channel channel;
    public MyConsumer(Channel channel){
        super(channel);
        this.channel = channel;
    }
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        super.handleDelivery(consumerTag, envelope, properties, body);
        System.err.println("---------consumer message--------");
        System.err.println("consumerTag:" + consumerTag);
        System.err.println("envelope:"+envelope);
        System.err.println("propertites:"+ properties);
        System.err.println("body"+ new String((body)));
        channel.basicQos(0,1,false);
        //消费端限流是手工签收 参数b：是否批量签收
        channel.basicAck(envelope.getDeliveryTag(),false);
    }
}
