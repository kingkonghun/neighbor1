package com.anabada.neighbor.member.domain;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Member {
    private long memberId; // 사용자 아이디
    private String memberEmail; // 이메일
    private String memberName; // 닉네임
    private String memberPWD; // 비밀번호
    private String address; // 지역
    private String addressDetail; // 상세주소
    private Date memberDate; // 가입날짜
    private MultipartFile profileImg; // 프로필이미지
    private String mbti; // mbit
    private int score; // 사용자점수
    private char memberStatus; // 회원상태
    private String providerId; // 로그인 정보
    private String role; // 권한
    private long myWrite;//내가 작성한 글


}
