package com.anabada.neighbor.used.service;

import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.used.domain.Post;
import com.anabada.neighbor.used.domain.Product;
import com.anabada.neighbor.used.domain.Used;
import com.anabada.neighbor.used.repository.UsedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsedServiceImpl implements UsedService{

    private final UsedRepository usedRepository;
    private final ImgDownService imgDownService;

    @Override
    public List<Used> list() {//글리스트
        List<Used> usedList = new ArrayList<>();
        List<Post> postList = usedRepository.postList();

        for (int i = 0; i < postList.size(); i++) {
            Post post = postList.get(i);
            Member member = usedRepository.findMember(post.getMemberId());
            Product product = usedRepository.findProduct(post.getPostId());
            String categoryName = usedRepository.findCategoryName(product.getCategoryId());

            Used used = Used.builder()
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .postType(post.getPostType())
                    .postDate(post.getPostDate())
                    .postUpdate(post.getPostUpdate())
                    .postView(post.getPostView())
                    .productId(product.getProductId())
                    .categoryName(categoryName)
                    .price(product.getPrice())
                    .productStatus(product.getProductStatus())
                    .categoryId(product.getCategoryId())
                    .memberId(member.getMemberId())
                    .address(member.getAddress())
                    .memberName(member.getMemberName())
                    .profileImg(member.getProfileImg())
                    .score(member.getScore())
                    .memberStatus(member.getMemberStatus())
                    .build();



            usedList.add(used);
        }

        return usedList;

    }

    @Transactional
    @Override
    public void write(Used used) {//글쓰기

        usedRepository.writePost(used);
        usedRepository.writeProduct(used);

        try {
            String uploadDir = "C:\\upload_anabada";

            if (!Files.exists(Paths.get(uploadDir))) {
                Files.createDirectories(Paths.get(uploadDir));
            }

            for (MultipartFile file : used.getFiles()) {
                String uuid = UUID.randomUUID().toString();
                String fileName = uuid + "_" + file.getOriginalFilename();
                String filePath = uploadDir + File.separator + fileName;
                file.transferTo(new File(filePath));
                usedRepository.writeImage(used.getPostId(),fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Used used) {//게시글수정
        usedRepository.updatePost(used);
        usedRepository.updateProduct(used);

        try {
            String uploadDir = "C:\\upload_anabada";

            if (!Files.exists(Paths.get(uploadDir))) {
                Files.createDirectories(Paths.get(uploadDir));
            }

            for (MultipartFile file : used.getFiles()) {
                String uuid = UUID.randomUUID().toString();
                String fileName = uuid + "_" + file.getOriginalFilename();
                String filePath = uploadDir + File.separator + fileName;
                file.transferTo(new File(filePath));
                usedRepository.updateImage(used.getPostId(),fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("서비스임플:"+used);
    }

    @Override
    public void delete(long postId) {
//        usedRepository.deletePost(postId);
//        usedRepository.deleteProdcut(postId);
//        usedRepository.delete
    }

    @Override
    public Used detail(long postId) {
        Post post = usedRepository.findPost(postId);
        Product product = usedRepository.findProduct(postId);
        String categoryName = usedRepository.findCategoryName(product.getCategoryId());
        Member member = usedRepository.findMember(post.getMemberId());
        return Used.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .postType(post.getPostType())
                .postDate(post.getPostDate())
                .postUpdate(post.getPostUpdate())
                .postView(post.getPostView())
                .productId(product.getProductId())
                .categoryName(categoryName)
                .price(product.getPrice())
                .productStatus(product.getProductStatus())
                .categoryId(product.getCategoryId())
                .memberId(member.getMemberId())
                .address(member.getAddress())
                .memberName(member.getMemberName())
                .profileImg(member.getProfileImg())
                .score(member.getScore())
                .memberStatus(member.getMemberStatus())
                .build();
    }

    @Override
    public String findImgUrl(long postId) {//사진
        String fileName = usedRepository.findImgUrl(postId);
        return fileName;
    }

    @Override
    public void downloadFiles(String filename, HttpServletResponse response) throws IOException {
            imgDownService.imgDown(filename,response);
        }

    }



