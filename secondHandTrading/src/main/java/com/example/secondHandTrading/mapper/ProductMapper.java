package com.example.secondHandTrading.mapper;

import com.example.secondHandTrading.entity.Product;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ProductMapper {

    // 1. 插入新商品
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO Product(title, price, description, status, seller_id, category) VALUES(#{title}, #{price}, #{description}, #{status}, #{sellerId}, #{category})")
    int insert(Product product);

    // 2. 购买商品（更新状态和买家）
    @Update("UPDATE Product SET status = 1, buyer_id = #{buyerId} WHERE id = #{id}")
    int updateStatusToSold(@Param("id") Integer id, @Param("buyerId") Integer buyerId);

    // 3. 查我发布的（排除已下架 -1 的）
    @Select("SELECT * FROM Product WHERE seller_id = #{userId} AND status != -1 ORDER BY id DESC")
    List<Product> findMyPublished(@Param("userId") Integer userId);

    // 4. 查我买到的
    @Select("SELECT * FROM Product WHERE buyer_id = #{userId} ORDER BY id DESC")
    List<Product> findMyBought(@Param("userId") Integer userId);

    // 5. 搜索（模糊匹配标题和描述，且仅查待售 0 的）
    @Select("SELECT * FROM Product WHERE status = 0 AND (title LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%')) ORDER BY id DESC")
    List<Product> searchProducts(@Param("keyword") String keyword);

    // 6. 查询所有待售商品（主页默认展示）
    @Select("SELECT * FROM Product WHERE status = 0 ORDER BY id DESC")
    List<Product> findAllActive();

    // 7. 逻辑删除（下架）
    @Update("UPDATE Product SET status = -1 WHERE id = #{id} AND seller_id = #{userId}")
    int deleteProduct(@Param("id") Integer id, @Param("userId") Integer userId);

    @Select("SELECT * FROM Product WHERE id = #{id}")
    Product findById(@Param("id") Integer id);
    // 根据 ID 查询单个商品详情
    // 动态查询待售商品（分页插件会自动在底层帮我们加上限制和总数统计）
    @Select("<script>" +
            "SELECT * FROM Product WHERE status = 0 " +
            "<if test='category != null and category != \"all\"'> AND category = #{category} </if> " +
            "ORDER BY id DESC" +
            "</script>")
    List<Product> findProductsByCategory(@Param("category") String category);
}