package com.anabada.neighbor.member.used.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Img {
    private long imgId;//이미지번호
    private long postId;//게시글번호
    private String imgUrl;//이미지url
}
