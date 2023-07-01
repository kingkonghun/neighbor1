package com.anabada.neighbor.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Chat {
    private long postId;
    private long master;
    private String title;
    private String price;
    private long roomId;
    private long receiver;
    private String receiverName;
    private long sender;
    private String senderName;
    private String content;
    private int chatCount;
    private String messageDate;
    private String messageType;
    private String productStatus;
    private String type;
    private List<Long> memberList;
    private int memberCount;
    private String hobbyName;
    private int maxMan;
    private int nowMan;
    private String chatMemberStatus;
}
