package com.anabada.neighbor.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChattingRoom {
    private long roomId;
    private long postId;
    private long creator;
    private String type;
    private String roomStatus;
}
