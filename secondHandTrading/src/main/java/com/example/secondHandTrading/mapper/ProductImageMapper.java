package com.example.secondHandTrading.mapper;

import com.example.secondHandTrading.entity.ProductImage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductImageMapper {
    // 批量插入图片（效率高）
    @Insert("<script>" +
            "INSERT INTO ProductImage(product_id, image_url) VALUES " +
            "<foreach collection='list' item='img' separator=','>" +
            "(#{img.productId}, #{img.imageUrl})" +
            "</foreach>" +
            "</script>")
    int insertBatch(@Param("list") List<ProductImage> images);

    // 根据商品ID查询所有图片
    @Select("SELECT * FROM ProductImage WHERE product_id = #{productId}")
    List<ProductImage> findByProductId(Integer productId);
}
