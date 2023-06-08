package com.anabada.neighbor.chat.service;

import com.anabada.neighbor.chat.config.SocketHandler;
import com.anabada.neighbor.chat.domain.ChatRoom;
import com.anabada.neighbor.chat.domain.ChatSession;
import com.anabada.neighbor.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.lang.model.SourceVersion;
import javax.servlet.http.HttpSession;
import java.util.*;


@RequiredArgsConstructor
@Service
public class ChatServiceImpl  implements ChatService {


    private final ChatRepository chatRepository;

    private static JSONObject jsonToObjectParser(String jsonStr) {
        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        try {
            obj = (JSONObject) parser.parse(jsonStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public void createRoom(HttpSession session) {//채팅방생성
        long memberId = 1L;
//        long memberId = (long)session.getAttribute("memberId");
        String memberName = "붕어빵";
//        String memberName = (String) session.getAttribute("memberName");
        String roomNumber = UUID.randomUUID().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("roomName", memberName);
        map.put("roomNumber", roomNumber);
        System.out.println("map = " + map);
        chatRepository.createRoom(map);
    }

    @Override
    public List<ChatRoom> findRoomNumber(HttpSession session) {
        long memberId = 1L;
//        long memberId = (long)session.getAttribute("memberId");
        List<ChatRoom> roomNumber = chatRepository.findRoomNumber(memberId);

        return roomNumber;
    }

    @Override
    public List<ChatSession> getSessionIds(String rN) {//웹소켓세션값가져오기
        List<ChatSession> sessionIds = chatRepository.getSessionIds(rN);
        for (int i = 0; i < sessionIds.size(); i++) {
            ChatSession chatSession = new ChatSession();
            chatSession.setSessionId(sessionIds.get(i).getSessionId());
            chatSession.setRoomNumber(sessionIds.get(i).getRoomNumber());
            System.out.println("chatSession = " + chatSession);
        }
        return sessionIds;
    }



    @Override
    public void removeSessionInfo(String sessionId) {
        chatRepository.removeSessionInfo(sessionId);
    }

    @Override
    public void saveSessionInfo(String roomNumber, String sessionId) {//웹소켓세션저장

        Map<String,String> map = new HashMap<>();
        map.put("roomNumber",roomNumber);
        map.put("sessionId", sessionId);
        chatRepository.saveSessionInfo(map);
    }

    @Override
    public String getRoomNumber(ChatSession sessionId) {

        return chatRepository.findRoomNumberBySessionId(sessionId);
    }

    @Override
    public WebSocketSession getWebSocketSession(ChatSession sessionId) {
        String sessionID = sessionId.getSessionId();
        WebSocketSession wss = chatRepository.getWebSocketSession(sessionID);
        return wss;
    }




}
