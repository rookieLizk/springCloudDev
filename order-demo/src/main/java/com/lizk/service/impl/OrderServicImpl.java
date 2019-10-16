package com.lizk.service.impl;

import com.lizk.hystrix.CacheCommand;
import com.lizk.hystrix.OrderCommand;
import com.lizk.hystrix.UserCommand;
import com.lizk.service.OrderServic;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class OrderServicImpl implements OrderServic {

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "userFallback")
    @Override
    public String getUser(int id) {
        // 获取用户信息
        String url = "http://lizk-user/user/{id}";
        String info = restTemplate.getForObject(url, String.class, id);
        return info;
    }


    // 添加服务器降级处理方法
    public String userFallback(int id) {
        return "error user fallback";
    }

    /**
     * 继承类的方式实现请求缓存
     * @param id
     * @return
     */
    @Override
    public String getUser2(Integer id) {

//        Long cacheKey = 999L + id;
        Long cacheKey = 999L;
        CacheCommand cacheCommand = new CacheCommand(cacheKey,restTemplate,id);
        String val = cacheCommand.execute();
        //清除缓存
        cacheCommand.clearRequestCache();
        return val;
    }

    /**
     * 注解的方式实现请求缓存
     * @param id
     * @param cacheKey
     * @return
     */
    @CacheResult
    @HystrixCommand(commandKey = "cache-user")
    @Override
    public String getUser3(Integer id,@CacheKey Long cacheKey) {
        String url = "http://lizk-user/user/{id}";
        String info = restTemplate.getForObject(url, String.class, id);
        System.out.println("查询方法结束========================");
//        clearRequestCache(cacheKey);
        return info;
    }

    /**
     * 清除基于注解的缓存
     * @param cacheKey
     */
    @CacheRemove(commandKey = "cache-user")
    @HystrixCommand
    public void clearRequestCache(@CacheKey Long cacheKey){
        System.out.println("调用基于注解实现的清除缓存方法=================");
    }


    // 测试依赖隔离
    @Override
    public String testPool() throws ExecutionException, InterruptedException {
        UserCommand userCommand = new UserCommand("库里");
        OrderCommand orderCommand1 = new OrderCommand("篮球");
        OrderCommand orderCommand2 = new OrderCommand("足球");
        // 同步调用
//        String val1 = userCommand.execute();
//        String val2 = orderCommand1.execute();
//        String val3 = orderCommand2.execute();
        // 异步调用
        Future<String> f1 = userCommand.queue();
        Future<String> f2 = orderCommand1.queue();
        Future<String> f3 = orderCommand1.queue();
//        return "val1=" + val1 + "val2=" + val2 + "val3=" + val3;
        return "f1=" + f1.get() + "f2=" + f2.get() + "f3=" + f3.get();

    }

    @HystrixCommand
    @Override
    public List<String> getUserAll(List<Integer> ids) {
        System.out.println("合并请求=======================");
        String url = "http://lizk-user/getUserAll?ids={1}";
        List<String> info = restTemplate.getForObject(url, List.class, StringUtils.join(ids,","));
        return info;
    }

    @HystrixCollapser(batchMethod = "getUserAll",collapserProperties={
            //请求时间间隔在5s之内的请求会被合并为一个请求,为方便手动测试,将两个浏览器的请求进行合并,将时间设置得比较大,实际中
            //需要设置得少,避免拉长每个请求的响应
            @HystrixProperty(name="timerDelayInMilliseconds", value="300")//,
            //在批处理中允许的最大请求数
//            @HystrixProperty(name="maxRequestsInBatch",value="200"),
    })
    @Override
    public Future<String> findOne(Integer id){
        System.out.println("被合并的单个请求");
        return null;
    }

}