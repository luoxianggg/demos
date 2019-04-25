package com.lx.project.demo3.controller;

import com.lx.project.demo3.model.Response;
import com.lx.project.demo3.model.order.Order;
import com.lx.project.demo3.service.OrderService;
import com.lx.project.demo3.service.SchehulerService;
import com.lx.project.demo3.util.OSSClientDisPartUploadUtil;
import com.lx.project.demo3.util.OSSClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/index")
public class demoController {
    @Autowired
    OrderService orderService;
    @Autowired
    SchehulerService schehulerService;

    @RequestMapping("/test")
    public Response test(@RequestBody Map<String,Object> map){
        Response response = new Response();
        //  rabbitMqConsumer.onOrderMessage();
        List<Order> list = orderService.queryOrders(map);
        return response.success(list);
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

    @RequestMapping("/fileUpload")
    public Response fileUpload(@RequestBody Map<String,Object> requestMap){
        Response response = new Response();
        String fileName = requestMap.get("fileName").toString();
        try {
            if(null != fileName || fileName != ""){
                if(!"".equals(fileName.trim())){

                    //上传到OSS
                    String url = OSSClientDisPartUploadUtil.uploadFile(fileName);
                }
            }
            return  response.success("上传成功！");
        }catch (Exception e){
            return response.failure("调用失败");
        }


    }
    @RequestMapping("/OSSTest")
  public Response uploadTest(@RequestBody Map<String,Object> map){
        Response response = new Response();
        OSSClientUtils.uploadHelloWorld();
        return response;
    }
}
