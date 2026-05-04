package com.example.secondHandTrading.service;

import com.example.secondHandTrading.entity.ChatMessage;
import com.example.secondHandTrading.entity.User;
import com.example.secondHandTrading.entity.Product;
import com.example.secondHandTrading.mapper.ChatMessageMapper;
import com.example.secondHandTrading.mapper.UserMapper;
import com.example.secondHandTrading.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatService {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    public ChatMessage saveMessage(ChatMessage message) {
        chatMessageMapper.insert(message);
        return message;
    }

    public List<ChatMessage> getChatHistory(Integer productId, Integer userId1, Integer userId2) {
        return chatMessageMapper.findConversation(productId, userId1, userId2);
    }

    public List<Map<String, Object>> getConversations(Integer userId) {
        List<ChatMessage> allMessages = chatMessageMapper.findAllByUser(userId);
        // Group by (product_id, other_user) and keep the latest message
        Map<String, ChatMessage> latestMap = new LinkedHashMap<>();
        for (ChatMessage msg : allMessages) {
            int otherUserId = msg.getSenderId().equals(userId) ? msg.getReceiverId() : msg.getSenderId();
            String key = msg.getProductId() + "_" + otherUserId;
            if (!latestMap.containsKey(key)) {
                latestMap.put(key, msg);
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, ChatMessage> entry : latestMap.entrySet()) {
            ChatMessage lastMsg = entry.getValue();
            int otherUserId = lastMsg.getSenderId().equals(userId) ? lastMsg.getReceiverId() : lastMsg.getSenderId();

            Map<String, Object> conv = new HashMap<>();
            conv.put("productId", lastMsg.getProductId());
            conv.put("otherUserId", otherUserId);
            conv.put("lastMessage", lastMsg);

            // Get user info
            User otherUser = userMapper.findById(otherUserId);
            conv.put("otherUserName", otherUser != null ? otherUser.getUsername() : "用户");
            conv.put("otherUserAvatar", otherUser != null ? otherUser.getAvatar() : null);

            // Get product info
            Product product = productMapper.findById(lastMsg.getProductId());
            conv.put("productTitle", product != null ? product.getTitle() : "商品");

            result.add(conv);
        }
        return result;
    }

    public void markAsRead(Integer productId, Integer receiverId, Integer senderId) {
        chatMessageMapper.markAsRead(productId, receiverId, senderId);
    }

    public int getUnreadCount(Integer userId) {
        return chatMessageMapper.countUnread(userId);
    }
}
