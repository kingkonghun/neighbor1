package com.anabada.neighbor.chat.handler;

import com.anabada.neighbor.chat.chatDomain.ChatMessage;
import com.anabada.neighbor.chat.chatDomain.ChatRoom;
import com.anabada.neighbor.chat.chatService.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
/*
* WebSocekt Handler
* socket통신은 서버와 클라가 1:n 관계. 한서버에 여러 클라이언트가 접속가능
* 서버에는 여러 클라가 발송한 메시지를 받아 처리해줄 handler의 작성필요
* TextWebSocketHandler를 상속받아 handler작성
* 클라가 받은 메시지를 log에 출력, 클라로 환영메시지를 보냄
* */
@Slf4j
@RequiredArgsConstructor
@Component
public class WebsocketChatHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}",payload);
//        TextMessage textMessage = new TextMessage("welcome chat server");
//        session.sendMessage(textMessage);
        /*
        * 채팅메시지를 객체로 변환, 전달받은 메시지에 담긴 채팅방id로 발송대상 채팅방 정보조회
        * 해당 채팅방에 입장해 있는 모든 클라이언트에게 타입에 따른 메시지 발송
        * */
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        ChatRoom room = chatService.findRoomById(chatMessage.getRoomId());
        room.handleActions(session, chatMessage, chatService);
    }
}
