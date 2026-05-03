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
        List<Product> list;
        if (keyword == null || keyword.trim().isEmpty()) {
            list = productMapper.findAllActive();
        } else {
            list = productMapper.searchProducts(keyword.trim());
        }
        // 新增：给搜索结果里的每个商品查图片
        for (Product product : list) {
            product.setImages(productImageMapper.findByProductId(product.getId()));
        }
        return list;
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
        PageHelper.startPage(pageNum, pageSize);
        List<Product> list = productMapper.findProductsByCategory(category);
        // 新增：给列表里的每个商品查询并附上图片
        for (Product product : list) {
            product.setImages(productImageMapper.findByProductId(product.getId()));
        }
        return new PageInfo<>(list);
    }
}