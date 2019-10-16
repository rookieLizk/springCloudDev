package com.lizk.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class TestController {

    @Value("${address}")
    private String test;

    /**
     * 返回配置文件中的值
     *
     **/
    @GetMapping("/value")
    @ResponseBody
    public String returnFormValue(){
        return test;
    }
}
