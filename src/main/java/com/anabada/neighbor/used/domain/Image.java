package com.anabada.neighbor.used.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {
            private int imgId;//이미지번호
            private int postId;//게시글번호
            private String imgUrl;//이미지url
}
