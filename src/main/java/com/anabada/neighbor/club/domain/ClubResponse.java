package com.anabada.neighbor.club.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClubResponse {
    private long postId; //게시글 번호
    private long memberId; // 작성자 아이디
    private String memberName; //작성자 닉네임
    private String title; //제목
    private String content; //글내용
    private String hobbyName; //취미이름
    private int score;  //사용자점수
    private int maxMan; //최대인원수
    private int nowMan; //현재인원수
    private List<MultipartFile> images = new ArrayList<>(); // 첨부파일 List

}