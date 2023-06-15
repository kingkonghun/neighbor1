package com.anabada.neighbor.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChatRoom {

   private long memberId;//사용자ID
   private String roomNumber;//방번호
   private Date roomTime;//채팅방만든시간
   private String roomName;//방이름
   private String sessionId;//웹소켓세션id
}
