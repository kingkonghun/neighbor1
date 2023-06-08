package com.anabada.neighbor.chat.controller;

import com.anabada.neighbor.chat.domain.ChatRoom;
import com.anabada.neighbor.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;
    @GetMapping("/chat")
    public String chat(Model model, @RequestParam String roomNumber) {
        model.addAttribute("roomNumber", roomNumber);
        return "chat/chat";
    }

    @GetMapping("/createRoom")
    public String createRoom(Model model, HttpSession session) {
        chatService.createRoom(session);
        List<ChatRoom> roomNumber= chatService.findRoomNumber(session);
        model.addAttribute("roomNumber", roomNumber);
        return "test";
    }


}
