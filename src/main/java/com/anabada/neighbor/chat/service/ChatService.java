package com.anabada.neighbor.chat.service;

import com.anabada.neighbor.chat.domain.ChatRoom;
import com.anabada.neighbor.chat.domain.ChatSession;
import com.anabada.neighbor.config.auth.PrincipalDetails;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

public interface ChatService {
    public void createRoom(PrincipalDetails principalDetails);

    List<ChatRoom> findRoomNumber(PrincipalDetails principalDetails);

    List<ChatSession> getSessionIds(String rN);

    void removeSessionInfo(String sessionId);

    void saveSessionInfo(String roomNumber, String sessionId);

    String getRoomNumber(ChatSession sessionId);

    WebSocketSession getWebSocketSession(ChatSession sessionId);

}
