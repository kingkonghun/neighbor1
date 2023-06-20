package com.anabada.neighbor.club.domain;

import java.time.LocalDateTime;

public class ImageResponse {
    private Long imgId;
    private Long postId;
    private String origName;
    private String saveName;
    private long size;
    private Boolean deleteYn;           //삭제여부
    private LocalDateTime creaDate;     //생성일시
    private LocalDateTime deleDate;     //삭제일시
}
