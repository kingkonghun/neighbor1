package com.anabada.neighbor.chat.service;

import com.anabada.neighbor.chat.domain.ChatRoom;
import com.anabada.neighbor.chat.domain.ChatSession;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

public interface ChatService {
    public void createRoom(HttpSession session);

    List<ChatRoom> findRoomNumber(HttpSession session);


    List<ChatSession> getSessionIds(String rN);


    void removeSessionInfo(String sessionId);

    void saveSessionInfo(String roomNumber, String sessionId);

    String getRoomNumber(ChatSession sessionId);

    WebSocketSession getWebSocketSession(ChatSession sessionId);

}
