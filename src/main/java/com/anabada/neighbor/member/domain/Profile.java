package com.anabada.neighbor.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Profile {
    private char m;
    private char b;
    private char t;
    private char i;
    private long memberId; //pk id
    private String memberName;      //닉네임
    private String memberPWD;       //pwd
    private String memberEmail;     //이메일
    private String address;         //짧은주소
    private String addressDetail;   //전체주소
    private Date memberDate;        //가입날짜
    private String profileImg;      //프로필 이미지
    private String mbti;            //mbti;
    private int score;              //사용자평점
    private char memberStatus;      //회원상태(가입,탈퇴,정지등)

    //내가 쓴 글을 위한..
    private long postId;
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
    private long categoryId;

    //내가 쓴 댓글을 위한..
    private long replyId; //댓글번호
    private String comment; //댓글
    private Date replyDate; //작성일자
    private Date replyUpdate; //수정일자
    private long parentId; //부모 게시글 번호
    private int depth; //댓글 깊이
    private int reGroup; //댓글 그룹


}
