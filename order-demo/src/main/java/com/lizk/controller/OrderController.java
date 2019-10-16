package com.lizk.controller;

import com.lizk.entity.User;
import com.lizk.service.FeignService;
import com.lizk.service.OrderServic;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class OrderController {

    @Autowired
    OrderServic orderService;

    @Autowired
    FeignService feignService;

    @RequestMapping("/order")
    public String addOrder(String name,int id) {
        // 调用用户，查询用户信息，
         String result = orderService.getUser(id);

        return "商品"+name+"，生成订单：" + result;
    }

    @RequestMapping("/order2")
    public String addOrder2(String name, int id) {
        // 调用用户，查询用户信息，
         String result = feignService.getUser(id);
         return "商品：" + name + "，生成订单：" + result;
    }

    @RequestMapping("/order3")
    public String addOrder3() {
        // 调用用户，查询用户信息，
        User user = new User();
        user.setId(1);
        user.setName("lzk");
        String result = feignService.getUser2(user);
        return "生成订单：" + result;
    }


    @RequestMapping("/pool")
    public String pool() throws ExecutionException, InterruptedException {
        // 调用用户，查询用户信息，
        String result = orderService.testPool();

        return result;
    }


    @RequestMapping("/cache")
    public String cache() {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        String result1 = orderService.getUser2(1);
        String result2 = orderService.getUser2(2);
        String result2_0 = orderService.getUser2(2);
        context.close();
//        String result3 = orderService.getUser2(3);
        return "result1: "+result1+"，result2：" + result2 +",result2_0："+result2_0;
    }

    @RequestMapping("/cache2")
    public String cache2() {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        String result1 = orderService.getUser3(1,12345L);
//        orderService.clearRequestCache(12345L);
        String result2 = orderService.getUser3(2,12345L);
        context.close();
//        String result3 = orderService.getUser2(3);
        return "cache2 result1: "+result1+"，cache2 result2：" + result2 ;
    }


    @RequestMapping("/findTest")
    public String findTest() throws ExecutionException, InterruptedException {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        Future<String> f1 = orderService.findOne(1);
        Future<String> f2 = orderService.findOne(2);
        Thread.sleep(1001);
        Future<String> f3 = orderService.findOne(3);
        String req = f1.get()+ "，" +f2.get() + "，" +f3.get();
        context.close();
        return req;
    }
}

