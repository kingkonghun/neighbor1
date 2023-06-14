package com.anabada.neighbor.club.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageRequest {
    private Long imgId;     //파일번호 pk
    private Long postId;    //게시글번호 fk
    private String origName;    //원본파일명
    private String saveName;    //저장파일명
    private long size;          //파일 크기
}
