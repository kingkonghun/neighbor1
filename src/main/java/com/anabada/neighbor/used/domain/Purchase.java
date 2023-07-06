package com.anabada.neighbor.used.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Purchase {
    private long purchaseId;
    private long postId;
    private long memberId;
}
