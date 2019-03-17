package com.lx.project.demo2.controller;

import com.lx.project.demo2.model.Response;
import com.lx.project.demo2.service.RabbitMqConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/")
public class consuserController {
    @Autowired
    RabbitMqConsumer rabbitMqConsumer;

    @RequestMapping("/consumerOrder")
    public Response consumerOrder(@RequestBody Map<String,Object> map){
        Response response = new Response();
      //  rabbitMqConsumer.onOrderMessage();
        return response.success("test");
    }
}
