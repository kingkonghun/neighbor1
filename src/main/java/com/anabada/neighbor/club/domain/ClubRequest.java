package com.anabada.neighbor.club.domain;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ClubRequest {
    private Long memberId; //Long 으로 하는이유는 프론트에서 null 값으로 들어올 수 있기 때문
    private String title;//제목
    private String content;//내용
    private String hobbyName;   //취미이름
    private Integer maxMan;     //최대인원수
    private List<MultipartFile> images = new ArrayList<>(); // 첨부파일 List
}
