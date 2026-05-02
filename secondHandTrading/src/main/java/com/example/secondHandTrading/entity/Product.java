package com.example.secondHandTrading.entity;
import java.math.BigDecimal;
import java.util.List;

public class Product {
    private Integer id;
    private String title;
    private BigDecimal price;
    private String description;
    private Integer status;
    private Integer sellerId; // 刚才我们在配置里开启了驼峰命名，这里会自动对应数据库里的 seller_id
    private Integer buyerId;
    private String category;
    private List<ProductImage> images;
    // 下面是基础的 Getter 和 Setter 方法，用于读取和修改属性
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getSellerId() { return sellerId; }
    public void setSellerId(Integer sellerId) { this.sellerId = sellerId; }

    public Integer getBuyerId() { return buyerId; }
    public void setBuyerId(Integer buyerId) { this.buyerId = buyerId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }
}