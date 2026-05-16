package com.example.secondHandTrading.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

// 🌟 新增导入 HttpServletRequest (注意：如果你用的是较老的 Spring Boot 2.x，请改成 javax.servlet.http.HttpServletRequest)
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@CrossOrigin
public class FileController {

    // 🌟 核心改动 1：在参数列表里加上 HttpServletRequest request
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return "error";
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            return "size_exceeded";
        }

        try {
            // 1. 获取原文件的后缀名 (例如 .jpg, .png)
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // 2. 生成一个全球唯一的随机文件名 (UUID)
            String newFileName = UUID.randomUUID().toString() + extension;

            // 3. 定义文件保存的物理路径：项目根目录下的 uploads 文件夹
            String dirPath = System.getProperty("user.dir") + "/uploads/";
            File dir = new File(dirPath);

            // 如果你电脑上还没有这个 uploads 文件夹，程序会自动帮你新建一个
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 4. 将内存中的文件真正保存到硬盘上的指定位置
            file.transferTo(new File(dirPath + newFileName));

            // 🌟 核心改动 2：动态获取协议、IP 和端口
            String scheme = request.getScheme();             // 获取协议 (http)
            String serverName = request.getServerName();     // 获取动态 IP 或域名 (如 10.240.165.107 或 localhost)
            int serverPort = request.getServerPort();        // 获取端口 (8080)

            // 5. 拼装出动态的网络访问地址
            String dynamicUrl = scheme + "://" + serverName + ":" + serverPort + "/uploads/" + newFileName;

            return dynamicUrl;

        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }
}