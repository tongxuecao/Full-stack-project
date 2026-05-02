package com.example.secondHandTrading.mapper;

// 重点：确保导入的是你自己写的那个User类
import com.example.secondHandTrading.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO [User] (username, password, phone, school_id) VALUES (#{username}, #{password}, #{phone}, #{schoolId})")
    int insert(User user);

    @Select("SELECT * FROM [User] WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM [User] WHERE id = #{id}")
    User findById(@Param("id") Integer id);
}
