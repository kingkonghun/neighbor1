package com.anabada.neighbor.reply.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CarryReply {
    private long replyId; //댓글번호
    private long memberId; //멤버아이디
    private long postId; //게시글번호
    private String comment; //댓글
    private Date replyDate; //작성일자
    private Date replyUpdate; //수정일자
    private long parentId; //부모 게시글 번호
    private int depth; //댓글 깊이
    private int reGroup; //댓글 그룹

    private String memberName; //닉네임
    private String parentName; //부모 닉네임

    private String title;//게시글 제목
    private String postType;//게시글 유형
}
