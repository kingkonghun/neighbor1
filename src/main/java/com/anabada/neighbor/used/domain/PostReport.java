package com.anabada.neighbor.used.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostReport {
    private long reportId; // 신고번호
    private long postId; // 게시물번호
    private long reporterId; // 고자질쟁이아이디
    private String reportedName;//신고당한사람
    private String content; // 신고내용
    private long reportTypeId; //신고타입번호
    private String reportTypeName; // 신고타입
    private String reporterName;//신고자닉네임
    private String title;
}
