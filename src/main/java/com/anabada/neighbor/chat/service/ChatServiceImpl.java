package com.anabada.neighbor.chat.service;

import com.anabada.neighbor.chat.config.SocketHandler;
import com.anabada.neighbor.chat.domain.ChatRoom;
import com.anabada.neighbor.chat.domain.ChatSession;
import com.anabada.neighbor.chat.repository.ChatRepository;
import com.anabada.neighbor.config.auth.PrincipalDetails;
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
    public void createRoom(PrincipalDetails principalDetails) {//채팅방생성

        ChatRoom chatRoom = ChatRoom.builder()
                .memberId(principalDetails.getMember().getMemberId())
                .roomName(principalDetails.getMember().getMemberName()) // 방번호 임시로 memberName 넣어둠
                .roomNumber(UUID.randomUUID().toString())
                .build();

//        long memberId = principalDetails.getMember().getMemberId();
//        String memberName = principalDetails.getMember().getMemberName();
//        String roomNumber = UUID.randomUUID().toString();
//        Map<String, Object> map = new HashMap<>();
//        map.put("memberId", memberId);
//        map.put("roomName", memberName);
//        map.put("roomNumber", roomNumber);
//        System.out.println("map = " + map);
        chatRepository.createRoom(chatRoom);
    }

    @Override
    public List<ChatRoom> findRoomNumber(PrincipalDetails principalDetails) {
        List<ChatRoom> roomNumber = chatRepository.findRoomNumber(principalDetails.getMember().getMemberId());
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
