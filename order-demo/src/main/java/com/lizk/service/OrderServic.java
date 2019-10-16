package com.lizk.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface OrderServic {
    public String getUser(int id);

    public String testPool() throws ExecutionException, InterruptedException;

    public String getUser2(Integer id);

    public String getUser3(Integer id,Long cacheKey);

    /**
     * 清除基于注解的缓存
     * @param cacheKey
     */
//    @CacheRemove(commandKey = "cache-user")
//    @HystrixCommand
    public void clearRequestCache(@CacheKey Long cacheKey);

    public List<String> getUserAll(List<Integer> ids);

    public Future<String> findOne(Integer id);
}
