package com.anabada.neighbor.club.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class PostRequest {
    private long postId;//pk
    private long memberId;//작성자아이디
    private String title;//제목
    private String content;//내용
    private String postType;//게시글타입
}
