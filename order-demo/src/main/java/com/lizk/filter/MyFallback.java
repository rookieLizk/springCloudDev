package com.lizk.filter;

import com.lizk.entity.User;
import com.lizk.service.FeignService;
import org.springframework.stereotype.Component;

@Component
public class MyFallback implements FeignService {
    @Override
    public String getUser(int id) {
        return "error getUser";
    }

    @Override
    public String getUser2(User user) {
        return "error getUser2";
    }
}
