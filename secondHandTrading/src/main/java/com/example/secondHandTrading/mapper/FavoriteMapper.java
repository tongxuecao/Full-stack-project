package com.example.secondHandTrading.mapper;

import com.example.secondHandTrading.entity.Favorite;
import com.example.secondHandTrading.entity.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FavoriteMapper {

    @Insert("INSERT INTO Favorite (user_id, product_id) VALUES (#{userId}, #{productId})")
    int insert(@Param("userId") Integer userId, @Param("productId") Integer productId);

    @Delete("DELETE FROM Favorite WHERE user_id = #{userId} AND product_id = #{productId}")
    int delete(@Param("userId") Integer userId, @Param("productId") Integer productId);

    @Select("SELECT * FROM Favorite WHERE user_id = #{userId} AND product_id = #{productId}")
    Favorite findByUserAndProduct(@Param("userId") Integer userId, @Param("productId") Integer productId);

    @Select("SELECT p.* FROM Product p INNER JOIN Favorite f ON p.id = f.product_id WHERE f.user_id = #{userId} ORDER BY f.id DESC")
    List<Product> findFavoritedProducts(@Param("userId") Integer userId);

    @Select("SELECT product_id FROM Favorite WHERE user_id = #{userId}")
    List<Integer> findFavoritedProductIds(@Param("userId") Integer userId);
}
