package com.anabada.neighbor.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatSession {
    private String roomNumber;
    private String sessionId;
    private long memberId;
}
