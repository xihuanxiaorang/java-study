package fun.xiaorang.springboot.annotation.controller;

import fun.xiaorang.springboot.annotation.service.OrderService;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import javax.inject.Named;


/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-awesome">java-awesome</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/5/17 10:45
 */
@Controller
public class OrderController {
    private OrderService orderService;

    public OrderService getOrderService() {
        return orderService;
    }

    @Inject
    @Named("orderServiceImpl2")
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String toString() {
        return "OrderController{" +
                "orderService=" + orderService +
                '}';
    }
}
