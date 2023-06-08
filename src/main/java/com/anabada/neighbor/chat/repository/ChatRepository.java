package com.anabada.neighbor.chat.repository;

import com.anabada.neighbor.chat.domain.ChatRoom;
import com.anabada.neighbor.chat.domain.ChatSession;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ChatRepository {
    public void createRoom(Map<String,Object> map);

    List<ChatRoom> findRoomNumber(long memberId);

    List<ChatSession> getSessionIds(String roomNumber);

    void saveSessionInfo(Map<String,String> map);

    void removeSessionInfo(String sessionId);

    String findRoomNumberBySessionId(ChatSession sessionId);


    WebSocketSession getWebSocketSession(String sessionID);

    void addRoomNumSession(HashMap<String, Object> map);
}
