package com.lx.project.demo1.service;


import com.lx.project.demo1.model.order.Order;


public interface OrderService {
    public void createOrder(Order order)throws Exception;
}
