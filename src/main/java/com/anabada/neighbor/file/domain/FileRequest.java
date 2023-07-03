package com.anabada.neighbor.file.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileRequest {
    private Long id;     //파일번호 pk
    private Long postId;    //게시글번호 fk
    private String originalName;    //원본파일명
    private String saveName;    //저장파일명
    private long size;          //파일 크기
}
