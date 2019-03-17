package com.lx.project.demo3.service;


import com.lx.project.demo3.model.order.Order;

import java.util.List;
import java.util.Map;


public interface OrderService {
    public List<Order> queryOrders(Map<String,Object> map);
    public void createOrder(Order order)throws Exception;

}
