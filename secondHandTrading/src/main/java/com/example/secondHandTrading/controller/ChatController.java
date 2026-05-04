package com.example.secondHandTrading.controller;

import com.example.secondHandTrading.entity.ChatMessage;
import com.example.secondHandTrading.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/history")
    public List<ChatMessage> getHistory(@RequestParam Integer productId,
                                         @RequestParam Integer userId1,
                                         @RequestParam Integer userId2) {
        return chatService.getChatHistory(productId, userId1, userId2);
    }

    @GetMapping("/conversations")
    public List<Map<String, Object>> getConversations(@RequestParam Integer userId) {
        return chatService.getConversations(userId);
    }

    @PostMapping("/read")
    public String markAsRead(@RequestBody Map<String, Object> body) {
        Integer productId = (Integer) body.get("productId");
        Integer receiverId = (Integer) body.get("receiverId");
        Integer senderId = (Integer) body.get("senderId");
        chatService.markAsRead(productId, receiverId, senderId);
        return "success";
    }

    @GetMapping("/unread")
    public int getUnreadCount(@RequestParam Integer userId) {
        return chatService.getUnreadCount(userId);
    }
}
