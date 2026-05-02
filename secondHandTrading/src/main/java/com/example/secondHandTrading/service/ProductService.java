package com.example.secondHandTrading.service;

import com.example.secondHandTrading.entity.Product;
import com.example.secondHandTrading.entity.ProductImage;
import com.example.secondHandTrading.mapper.ProductImageMapper;
import com.example.secondHandTrading.mapper.ProductMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    // 1. 发布商品业务逻辑
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductImageMapper productImageMapper;

    @Transactional // 开启事务，保证原子性
    public boolean publishProduct(Product product, List<String> imageUrls) {
        // 1. 保存商品基本信息 (MyBatis会自动把生成的ID写回product对象的id属性)
        product.setStatus(0);
        int result = productMapper.insert(product);

        // 2. 如果基本信息保存成功且有图片，则保存图片
        if (result > 0 && imageUrls != null && !imageUrls.isEmpty()) {
            List<ProductImage> images = new ArrayList<>();
            for (String url : imageUrls) {
                ProductImage pi = new ProductImage();
                pi.setProductId(product.getId()); // 关键：拿到刚才生成的商品ID
                pi.setImageUrl(url);
                images.add(pi);
            }
            productImageMapper.insertBatch(images);
        }
        return result > 0;
    }

    // 2. 购买商品业务逻辑
    public boolean buyProduct(Integer productId, Integer buyerId) {
        return productMapper.updateStatusToSold(productId, buyerId) > 0;
    }

    // 3. 获取我发布的
    public List<Product> getMyPublished(Integer userId) {
        return productMapper.findMyPublished(userId);
    }

    // 4. 获取我买到的
    public List<Product> getMyBought(Integer userId) {
        return productMapper.findMyBought(userId);
    }

    // 5. 搜索业务逻辑
    public List<Product> search(String keyword) {
        // 业务判断：如果没传关键词，或者只有空格，就直接返回所有待售商品
        if (keyword == null || keyword.trim().isEmpty()) {
            return productMapper.findAllActive();
        }
        return productMapper.searchProducts(keyword.trim());
    }

    // 6. 下架业务逻辑
    public boolean deleteProduct(Integer productId, Integer userId) {
        return productMapper.deleteProduct(productId, userId) > 0;
    }

    // 7.  获取商品详情
    public Product getProductById(Integer id) {
        Product product = productMapper.findById(id);
        if (product != null) {
            product.setImages(productImageMapper.findByProductId(id));
        }
        return product;
    }

    public PageInfo<Product> getProductPage(int pageNum, int pageSize, String category) {
        // 核心魔法：这一行会告诉 MyBatis，紧接着的下一个查询需要进行分页
        PageHelper.startPage(pageNum, pageSize);
        // 执行查询
        List<Product> list = productMapper.findProductsByCategory(category);
        // 用 PageInfo 包装一下结果，它里面包含了总页数、总条数等丰富信息
        return new PageInfo<>(list);
    }
}