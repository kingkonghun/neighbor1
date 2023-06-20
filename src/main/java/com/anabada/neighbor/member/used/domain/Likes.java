package com.anabada.neighbor.member.used.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Likes {
    private long likeId; // 좋아요번호
    private long postId; // 게시물번호
    private long memberId; // 사용자아이디
}
