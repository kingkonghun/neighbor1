package com.anabada.neighbor.club.domain;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClubPost {
    private long memberId;          //사용자id
    private long postId;//게시글번호
    private String memberName;  //닉네임
    private String address;         //짧은주소
    private String profileImg;      //프로필 이미지
    private String mbti;            //mbti;
    private int score;             //사용자점수
    private String title;//제목
    private String content;//내용
    private String postType;//게시글타입
    private Date postDate;//생성일자
    private Date postUpdate;//수정일자
    private long postView;//조회수
}
