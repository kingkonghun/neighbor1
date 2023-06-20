package com.anabada.neighbor.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Chatting {
   private String roomNumber;//방번호
   private long memberId;//멤버ID
   private String chatMsg;//대화내용
   private Date chatTime;//대화시간
}
