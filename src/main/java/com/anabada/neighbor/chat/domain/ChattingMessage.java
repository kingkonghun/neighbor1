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
public class ChattingMessage {
    private long messageId;
    private long roomId;
    private long writer;
    private String content;
    private Date messageDate;
    private String messageType;
}
