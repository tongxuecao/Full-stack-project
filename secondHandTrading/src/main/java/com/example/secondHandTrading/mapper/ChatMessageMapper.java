package com.example.secondHandTrading.mapper;

import com.example.secondHandTrading.entity.ChatMessage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChatMessageMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO ChatMessage (product_id, sender_id, receiver_id, content) VALUES (#{productId}, #{senderId}, #{receiverId}, #{content})")
    int insert(ChatMessage message);

    @Select("SELECT * FROM ChatMessage WHERE product_id = #{productId} AND ((sender_id = #{userId1} AND receiver_id = #{userId2}) OR (sender_id = #{userId2} AND receiver_id = #{userId1})) ORDER BY create_time ASC")
    List<ChatMessage> findConversation(@Param("productId") Integer productId, @Param("userId1") Integer userId1, @Param("userId2") Integer userId2);

    @Select("SELECT * FROM ChatMessage WHERE sender_id = #{userId} OR receiver_id = #{userId} ORDER BY create_time DESC")
    List<ChatMessage> findAllByUser(@Param("userId") Integer userId);

    @Update("UPDATE ChatMessage SET is_read = 1 WHERE product_id = #{productId} AND receiver_id = #{receiverId} AND sender_id = #{senderId} AND is_read = 0")
    int markAsRead(@Param("productId") Integer productId, @Param("receiverId") Integer receiverId, @Param("senderId") Integer senderId);

    @Select("SELECT COUNT(*) FROM ChatMessage WHERE receiver_id = #{userId} AND is_read = 0")
    int countUnread(@Param("userId") Integer userId);
}
