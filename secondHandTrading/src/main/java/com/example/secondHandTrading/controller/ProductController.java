package com.example.secondHandTrading.controller;

import com.example.secondHandTrading.entity.Product;
import com.example.secondHandTrading.mapper.ProductMapper;
import com.example.secondHandTrading.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProductController {

    // 注入的是 Service，而不是 Mapper
    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public String addProduct(@RequestBody Map<String, Object> requestData) {
        // 1. 从 Map 中手动解析出商品信息
        // 注意：这里需要根据你的 JSON 结构转换，或者创建一个专门的 ProductDTO 类
        Product product = new Product();
        product.setTitle((String) requestData.get("title"));
        product.setPrice(new BigDecimal(requestData.get("price").toString()));
        product.setDescription((String) requestData.get("description"));
        product.setSellerId(Integer.valueOf(requestData.get("sellerId").toString()));
        product.setCategory((String) requestData.get("category"));

        // 2. 解析图片 URL 列表
        List<String> imageUrls = (List<String>) requestData.get("imageUrls");

        return productService.publishProduct(product, imageUrls) ? "success" : "fail";
    }

    @PostMapping("/buy/{id}")
    public String buyProduct(@PathVariable Integer id, @RequestParam Integer buyerId) {
        return productService.buyProduct(id, buyerId) ? "success" : "fail";
    }

    @GetMapping("/my-published")
    public List<Product> getMyPublished(@RequestParam Integer userId) {
        return productService.getMyPublished(userId);
    }

    @GetMapping("/my-bought")
    public List<Product> getMyBought(@RequestParam Integer userId) {
        return productService.getMyBought(userId);
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam(required = false) String keyword) {
        return productService.search(keyword);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Integer id, @RequestParam Integer userId) {
        return productService.deleteProduct(id, userId) ? "success" : "fail";
    }
    @GetMapping("/detail/{id}")
    public Product getProductById(@PathVariable Integer id) {
        return productService.getProductById(id);
    }
    @GetMapping("/page")
    public PageInfo<Product> getProductPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "8") int pageSize, // 假设每页展示 8 个
            @RequestParam(defaultValue = "all") String category) {
        return productService.getProductPage(pageNum, pageSize, category);
    }
}