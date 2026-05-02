package com.example.secondHandTrading.controller;

import com.example.secondHandTrading.entity.User;
import com.example.secondHandTrading.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    // 【修改】注入 UserService，而不是 UserMapper
    @Autowired
    private UserService userService;

    // 1. 注册接口
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return userService.register(user);
    }

    // 2. 登录接口
    @PostMapping("/login")
    public User login(@RequestBody User loginUser) {
        return userService.login(loginUser.getUsername(), loginUser.getPassword());
    }

    @GetMapping("/info/{id}")
    public User getUserInfo(@PathVariable Integer id) {
        // 同样，路由只负责接收参数和返回结果
        return userService.getUserInfo(id);
    }

}