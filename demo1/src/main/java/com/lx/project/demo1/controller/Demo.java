package com.lx.project.demo1.controller;

import com.github.pagehelper.PageInfo;
import com.lx.project.demo1.model.JobAndTrigger;
import com.lx.project.demo1.model.Response;
import com.lx.project.demo1.model.order.Order;
import com.lx.project.demo1.service.OrderService;
import com.lx.project.demo1.service.RabbitOrderSender;
import com.lx.project.demo1.service.SchehulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/index")
public class Demo {

    /*@Autowired
    RedisTemplate redisTemplate;*/
    @Autowired
    OrderService orderService;
    @Autowired
    RabbitOrderSender rabbitOrderSender;
    @Autowired
    SchehulerService schehulerService;


    @RequestMapping("/test")
    public Response test(@RequestBody Map<String,Object> map){
        Response response = new Response();
        //  rabbitMqConsumer.onOrderMessage();
        return response.success("test");
    }
    @RequestMapping("/testSenderOrder")
    public HashMap<String,Object> testSenderOrder(@RequestBody Map<String,Object> map)throws Exception{
        HashMap<String,Object> res = new HashMap<String, Object>();
        Order order = new Order();
        System.out.println(map.get("id").toString());
        order.setId(map.get("id").toString());
        order.setName(map.get("name").toString());
        order.setMessageId(System.currentTimeMillis()+'$'+ UUID.randomUUID().toString());

      // rabbitOrderSender.senderOrder(order);

        orderService.createOrder(order);

        res.put("msg","订单发送成功,订单号："+ order.getId());
        return res;
    }
    @RequestMapping("/addAQuartzJob")
    public void addAQuartzJob(@RequestBody Map<String,Object> map)throws Exception{

        schehulerService.addJobs(map);
    }
    @RequestMapping("/deleteAQuartzJob")
    public void deleteAQuartzJob(@RequestBody Map<String,Object> map)throws Exception{

        schehulerService.deleteJob(map);
    }
    @RequestMapping("/pauseAQuartzJob")
    public void pauseAQuartzJob(@RequestBody Map<String,Object> map)throws Exception{

        schehulerService.pauseJob(map);
    }
    @RequestMapping("/resumeAQuartzJob")
    public void resumeAQuartzJob(@RequestBody Map<String,Object> map)throws Exception{

        schehulerService.resumeJob(map);
    }
    @RequestMapping("/reSchedulerAQuartzJob")
    public void reScheulerAQuartzJob(@RequestBody Map<String,Object> map)throws Exception{

        schehulerService.reSchedulerJob(map);
    }
    @RequestMapping("/queryAQuartzJob")
    public Response queryAQuartzJob(@RequestBody Map<String,Object> map)throws Exception{

        Response response = new Response();
        Map<String,Object>  map1 = new HashMap<String,Object>();
        map1.put("jobs",schehulerService.queryJobs(map).get("jobAndTrigger"));
        map1.put("count",schehulerService.queryJobs(map).get("totalCount"));
        return response.success(map1);
    }
  /*   @RequestMapping("/redisTest1")
     public HashMap<String,Object> redisTest(HttpServletRequest request, @RequestBody Map<String,Object> map){
        map.put("name","好汉饶命");
         HashMap<String,Object> res = new HashMap<String, Object>();
     	Test redis1 = testService.RedisTest(map);
     	res.put("notice","若下面没出现“无缓存的时候调用”字样且能打印出数据表示测试成功");
     	return res;
     }
      @RequestMapping("/redisTest2")
     public HashMap<String,Object> redisTest2(HttpServletRequest request, @RequestBody Map<String,Object> map){
        map.put("name","好汉饶命");
         HashMap<String,Object> res = new HashMap<String, Object>();
     	Test redis1 = testService.RedisTest(map);
     	res.put("notice1","我这里没执行查询");
        res.put("name:",redis1.getName());
        return res;
     }
    @RequestMapping("/redisTest3")
    public HashMap<String,Object> redisTest3(HttpServletRequest request, @RequestBody Map<String,Object> map){
        HashMap<String,Object> res = new HashMap<String, Object>();
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        operations.set("name","luo");
        res.put("name:",operations.get("name"));
       // res.put("bar",operations);
        System.out.println(res);
        return res;
    }*/
}
