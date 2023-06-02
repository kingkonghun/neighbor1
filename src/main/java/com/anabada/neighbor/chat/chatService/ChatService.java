package com.anabada.neighbor.chat.chatService;

import com.anabada.neighbor.chat.chatDomain.ChatRoom;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

public interface ChatService {
    public List<ChatRoom> findAllRoom();

    public ChatRoom findRoomById(String roomId);

    public ChatRoom createRoom(String name);

    public <T> void sendMessage(WebSocketSession session, T message);
}
