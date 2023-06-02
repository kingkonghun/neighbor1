package com.anabada.neighbor.chat.chatDomain;


import lombok.*;

@Getter
@Setter
public class ChatMessage {
    public enum MessageType{//enter:채팅방입장/talk:메시지보내기
        ENTER, TALK
    }
    private MessageType type; //메시지타입
    private String roomId;//방번호
    private String sender;//메시지 보낸사람
    private String message;//메시지내용
}
