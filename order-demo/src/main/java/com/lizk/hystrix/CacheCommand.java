package com.lizk.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import org.springframework.web.client.RestTemplate;

public class CacheCommand extends HystrixCommand<String> {

    private Long cacheKey;
    private RestTemplate restTemplate;
    private Integer uid;
    public CacheCommand(Long cacheKey, RestTemplate restTemplate,Integer uid) {
        super(Setter.withGroupKey(
                HystrixCommandGroupKey.Factory.asKey("cache-group")
        ).andCommandKey(HystrixCommandKey.Factory.asKey("cache-test")));
        this.restTemplate = restTemplate;
        this.cacheKey = cacheKey;
        this.uid = uid;
    }

    @Override
    protected String run() throws Exception {
        System.out.println("没有缓存，查询数据============================");
        String url = "http://lizk-user/user/{id}";
        String info = restTemplate.getForObject(url, String.class, uid);
        return info;
    }

    @Override
    protected String getCacheKey(){
        return String.valueOf(cacheKey);
    }

    public void clearRequestCache(){
        HystrixRequestCache.getInstance(
          HystrixCommandKey.Factory.asKey("cache-test"), HystrixConcurrencyStrategyDefault.getInstance())
                .clear(String.valueOf(cacheKey));
    }
}
