package com.anabada.neighbor.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Chat {
    private long roomId;
    private long receiver;
    private String receiverName;
    private long sender;
    private String senderName;
    private String content;
    private int chatCount;
    private String messageDate;
    private String messageType;
}
