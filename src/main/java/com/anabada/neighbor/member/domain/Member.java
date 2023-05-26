package com.anabada.neighbor.member.domain;

import lombok.*;


import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Member {
    private long memberId; //pk id
    private String memberName;      //닉네임
    private String memberPWD;       //pwd
    private String address;         //짧은주소
    private String addressDetail;   //전체주소
    private Date memberDate;        //가입날짜
    private String profileImg;      //프로필 이미지
    private String mbti;            //mbti;
    private int score;              //사용자평점
    private char memberStatus;      //회원상태(가입,탈퇴,정지등)
}
