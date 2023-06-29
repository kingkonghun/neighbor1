package com.anabada.neighbor.used.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Sales {
    private long salesId;
    private long postId;
    private long memberId;
}
