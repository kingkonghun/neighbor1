package com.anabada.neighbor.file.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FileResponse {
    private Long id;
    private Long postId;
    private String originalName;
    private String saveName;
    private long size;
    private Boolean deleteYn;           //삭제여부
    private LocalDateTime createdDate;     //생성일시
    private LocalDateTime deletedDate;      //삭제일시
}
