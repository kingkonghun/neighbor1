package com.anabada.neighbor.club;

import com.anabada.neighbor.file.domain.FileResponse;
import com.anabada.neighbor.club.repository.ClubRepository;
import com.anabada.neighbor.used.domain.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
public class ClubTest {
    private final String uploadPath = Paths.get("/Users", "upload_anabada").toString();

    @Autowired
    ClubRepository clubRepository;

    @Test
    void save(){
        Post post = Post.builder()
                .title("tit")
                .content("con")
                .memberId(1)
                .postType("used")
                .build();
//        clubRepository.insertPost(post);
        List<Post> posts = clubRepository.selectPostList();
        System.out.println("전체 게시글 개수는 : " + posts.size());
    }

    @Test
    void selectPost(){
        Post post = clubRepository.selectPost(1L);
        try {
            String postJson = new ObjectMapper().registerModule(new JavaTimeModule())
                    .writeValueAsString(post);
            System.out.println(postJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void update(){
        // 1. 게시글 수정
        Post params = Post.builder()
                .postId(1L)
                .title("수정")
                .content("수정콘텐")
                .build();
        clubRepository.updatePost(params);
        // 1. 수정한 게시글 확인
        Post post = clubRepository.selectPost(1L);
        try {
            String postJson = new ObjectMapper().registerModule(new JavaTimeModule())
                    .writeValueAsString(post);
            System.out.println(postJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void delete(){
        System.out.println("삭제 이전의 전체클럽 게시글 수는 : " + clubRepository.selectPostList().size());
        clubRepository.deletePost(1L);
        System.out.println("삭제 이후 전체클럽 게시글 수는 : " + clubRepository.selectPostList().size());

    }

    @Test
    public Stream<Path> loadAll(FileResponse file) {
        String uploadedDate = file.getCreatedDate().toLocalDate().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String filename = file.getSaveName();
        Path filePath = Paths.get(uploadPath, uploadedDate, filename);
        System.out.println("update : " + uploadedDate);
        System.out.println("name : " + filename);
        System.out.println("path : " + filePath.toString());

        try {
            return Files.walk(filePath, 1)
                    .filter(path -> !path.equals(filePath)).map(filePath::relativize);
        } catch (IOException e) {
            throw new RuntimeException(e) ;
        }

    }
}
