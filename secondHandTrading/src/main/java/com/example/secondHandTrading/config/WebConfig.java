package com.example.secondHandTrading.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取当前项目的根目录路径，并在后面拼上 uploads/ 文件夹
        String path = System.getProperty("user.dir") + "/uploads/";

        // 映射规则：如果浏览器请求的网址是以 /uploads/ 开头的
        // Spring Boot 就会自动去项目根目录下的 uploads 文件夹里找对应的文件
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + path);
    }
}