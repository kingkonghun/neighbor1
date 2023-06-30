package com.anabada.neighbor.club.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClubJoin {
    private Long id;
    private Long clubId;
    private Long memberId;
}
