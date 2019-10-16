package com.lizk.controller;

import com.lizk.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api("用户接口")
@RestController
public class UserController {

    @Value("${server.port}")
    private int port;

//    @RequestMapping("/user/{id}")
    @ApiOperation(value="根据用户id查询用户信息" ,notes = "根据用户id查询用户信息")
    @GetMapping("/user/{id}")
    public String getUser(@PathVariable("id") int id){
        String name = "";
        if(id == 1){
           name = "123";
        } else if(id == 2){
            name = "456";
        } else {
            name = "789";
        }
        System.out.println(name);
        return name+"端口号："+port;
    }


    /**
     * 查询所有用户信息
     * @param ids
     * @return
     */
    @ApiOperation(value="查询所有用户信息" ,notes = "查询所有用户信息")
    @GetMapping("/getUserAll")
    public List<String> getUserAll(@RequestParam List<Integer> ids){
        ArrayList<String> list = new ArrayList<>();
        ids.forEach(e->{
            list.add("用户："+e);
        });
        return list;
    }


//    @RequestMapping("/user/{id}")
    @ApiOperation(value="返回用户信息" ,notes = "根据user返回数据")
    @ApiImplicitParam(name = "user",value = "用户信息",required = true,dataType = "User")
    @PostMapping("/user2")
    public String getUser2(User user){
        return "OK："+user;
    }
}
