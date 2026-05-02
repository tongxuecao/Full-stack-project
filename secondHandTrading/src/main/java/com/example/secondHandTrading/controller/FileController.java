package com.example.secondHandTrading.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@CrossOrigin
public class FileController {

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        // 如果文件是空的，直接返回错误
        if (file.isEmpty()) {
            return "error";
        }

        try {
            // 1. 获取原文件的后缀名 (例如 .jpg, .png)
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // 2. 生成一个全球唯一的随机文件名 (UUID)，防止不同用户上传同名文件导致互相覆盖
            String newFileName = UUID.randomUUID().toString() + extension;

            // 3. 定义文件保存的物理路径：项目根目录下的 uploads 文件夹
            String dirPath = System.getProperty("user.dir") + "/uploads/";
            File dir = new File(dirPath);

            // 如果你电脑上还没有这个 uploads 文件夹，程序会自动帮你新建一个
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 4. 核心执行代码：将内存中的文件真正保存到硬盘上的指定位置
            file.transferTo(new File(dirPath + newFileName));

            // 5. 拼装出完整的网络访问地址，并返回给前端 (例如: http://localhost:8080/uploads/abc-123.jpg)
            return "http://localhost:8080/uploads/" + newFileName;

        } catch (IOException e) {
            // 打印错误日志到控制台，方便排查
            e.printStackTrace();
            return "error";
        }
    }
}