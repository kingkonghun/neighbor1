package com.anabada.neighbor.used.domain;

import lombok.*;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Used {
    private String title;//제목
    private String content;//내용
    private String postType;//게시글타입
    private Date postDate;//생성일자
    private Date postUpdate;//수정일자
    private long postView;//조회수

    private long productId; //상품아이디
    private String categoryName; //카테고리이름
    private String price; //상품가격
    private String productStatus; //판매여부


    private long memberId; //pk id
    private String address;
    private String memberName;      //닉네임
    private String profileImg;      //프로필 이미지
    private int score;              //사용자평점
    private char memberStatus;      //회원상태(가입,탈퇴,정지등)

}


