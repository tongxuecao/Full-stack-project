package com.example.secondHandTrading.service;

import com.example.secondHandTrading.entity.User;
import com.example.secondHandTrading.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    // 注册业务逻辑
    public String register(User user) {
        // 先检查用户名是否被占用
        if (userMapper.findByUsername(user.getUsername()) != null) {
            return "exists";
        }
        int result = userMapper.insert(user);
        return result > 0 ? "success" : "fail";
    }

    // 登录业务逻辑
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        // 校验密码
        if (user != null && user.getPassword().equals(password)) {
            // 【安全规范】登录成功后，擦除密码再返回给前端，防止密码泄露
            user.setPassword(null);
            return user;
        }
        return null; // 登录失败
    }

    // 获取用户信息（用于详情页展示卖家信息）
    public User getUserInfo(Integer id) {
        User user = userMapper.findById(id);
        // 业务逻辑：安全第一，绝对不能把密码传给前端
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    public String updateAvatar(Integer id, String avatar) {
        return userMapper.updateAvatar(id, avatar) > 0 ? "success" : "fail";
    }

    public String updateUsername(Integer id, String username) {
        User existing = userMapper.findByUsername(username);
        if (existing != null && !existing.getId().equals(id)) {
            return "exists";
        }
        return userMapper.updateUsername(id, username) > 0 ? "success" : "fail";
    }

    public String updatePassword(Integer id, String oldPassword, String newPassword) {
        User user = userMapper.findById(id);
        if (user == null || !user.getPassword().equals(oldPassword)) {
            return "wrong_password";
        }
        return userMapper.updatePassword(id, newPassword) > 0 ? "success" : "fail";
    }
}