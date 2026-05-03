package com.example.secondHandTrading.mapper;

// 重点：确保导入的是你自己写的那个User类
import com.example.secondHandTrading.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO [User] (username, password, phone, school_id) VALUES (#{username}, #{password}, #{phone}, #{schoolId})")
    int insert(User user);

    @Select("SELECT * FROM [User] WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM [User] WHERE id = #{id}")
    User findById(@Param("id") Integer id);

    @Update("UPDATE [User] SET avatar = #{avatar} WHERE id = #{id}")
    int updateAvatar(@Param("id") Integer id, @Param("avatar") String avatar);

    @Update("UPDATE [User] SET username = #{username} WHERE id = #{id}")
    int updateUsername(@Param("id") Integer id, @Param("username") String username);

    @Update("UPDATE [User] SET password = #{password} WHERE id = #{id}")
    int updatePassword(@Param("id") Integer id, @Param("password") String password);
}
