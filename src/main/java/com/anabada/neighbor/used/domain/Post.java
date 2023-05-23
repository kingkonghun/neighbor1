package com.anabada.neighbor.used.domain;

import lombok.*;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Post extends Used {
    	private long postId;//게시글번호
        private long memberId;//작성자아이디
        private String title;//제목
        private String content;//내용
        private String postType;//게시글타입
        private Date postDate;//생성일자
        private Date postUpdate;//수정일자
        private long postView;//조회수
}
