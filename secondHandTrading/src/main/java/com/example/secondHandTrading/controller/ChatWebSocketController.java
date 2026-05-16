package com.example.secondHandTrading.controller;

import com.example.secondHandTrading.entity.ChatMessage;
import com.example.secondHandTrading.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessage message) {
        ChatMessage saved = chatService.saveMessage(message);//存到数据库

        messagingTemplate.convertAndSend("/topic/chat." + message.getReceiverId(), saved);
        messagingTemplate.convertAndSend("/topic/chat." + message.getSenderId(), saved);
    }
}
