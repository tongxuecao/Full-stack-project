package com.example.secondHandTrading.controller;

import com.example.secondHandTrading.entity.User;
import com.example.secondHandTrading.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

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
        return userService.getUserInfo(id);
    }

    @PutMapping("/avatar")
    public String updateAvatar(@RequestBody Map<String, Object> body) {
        Integer id = (Integer) body.get("id");
        String avatarUrl = (String) body.get("avatarUrl");
        return userService.updateAvatar(id, avatarUrl);
    }

    @PutMapping("/username")
    public String updateUsername(@RequestBody Map<String, Object> body) {
        Integer id = (Integer) body.get("id");
        String username = (String) body.get("username");
        return userService.updateUsername(id, username);
    }

    @PutMapping("/password")
    public String updatePassword(@RequestBody Map<String, Object> body) {
        Integer id = (Integer) body.get("id");
        String oldPassword = (String) body.get("oldPassword");
        String newPassword = (String) body.get("newPassword");
        return userService.updatePassword(id, oldPassword, newPassword);
    }

    @PostMapping("/avatar/upload")
    public String uploadAvatar(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "error";
        }
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFileName = UUID.randomUUID().toString() + extension;
            String dirPath = System.getProperty("user.dir") + "/uploads/";
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file.transferTo(new File(dirPath + newFileName));
            return "http://localhost:8080/uploads/" + newFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/refresh/{id}")
    public User refreshUser(@PathVariable Integer id) {
        return userService.getUserInfo(id);
    }

}