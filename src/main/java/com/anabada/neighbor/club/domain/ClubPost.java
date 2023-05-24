package com.anabada.neighbor.club.domain;

import lombok.*;

@Data
@Builder
public class ClubPost {
    private Long memberId;
    private String title;//제목
    private String content;//내용
    private String hobbyName;   //취미이름
    private Integer maxMan;     //최대인원수
}
