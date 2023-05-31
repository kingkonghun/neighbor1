package com.anabada.neighbor.used.service;

import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.used.domain.Category;
import com.anabada.neighbor.used.domain.Post;
import com.anabada.neighbor.used.domain.Product;
import com.anabada.neighbor.used.domain.Used;
import com.anabada.neighbor.used.repository.UsedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
    public void update(Used used) throws Exception{//게시글수정
        String formImg = used.getFiles().get(0).getOriginalFilename();
        usedRepository.updatePost(used);
        usedRepository.updateProduct(used);
        String uploadDir = "C:\\upload_anabada";
        Path originImg = Path.of(uploadDir+"\\"+usedRepository.findImgUrl(used.getPostId()));//원래 이미지 url찾아오기
            if (!Files.exists(Paths.get(uploadDir))) {//디렉토리가 없다면 디렉토리생성
                Files.createDirectories(Paths.get(uploadDir));
            }
            if(!formImg.equals("") && formImg != null ) {
                for (MultipartFile file : used.getFiles()) {
                    System.out.println("이프문안에:"+formImg);
                    Files.delete(originImg);//원래 이미지 삭제
                    String uuid = UUID.randomUUID().toString();
                    String fileName = uuid + "_" + file.getOriginalFilename();
                    String filePath = uploadDir + File.separator + fileName;
                    file.transferTo(new File(filePath));
                    usedRepository.updateImage(used.getPostId(), fileName);
                  }
            }


    }

    @Override
    public void delete(long postId) {

        try {
            Path uploadDir = Path.of("C:\\upload_anabada\\"+usedRepository.findImgUrl(postId));
            Files.delete(uploadDir);
            usedRepository.deleteReply(postId);
            usedRepository.deleteImg(postId);
            usedRepository.deleteProduct(postId);
            usedRepository.deletePost(postId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Used detail(long postId, HttpServletRequest request, HttpServletResponse response) {
        Post post = usedRepository.findPost(postId);
        Product product = usedRepository.findProduct(postId);
        String categoryName = usedRepository.findCategoryName(product.getCategoryId());
        Member member = usedRepository.findMember(post.getMemberId());

        Cookie[] cookies = request.getCookies();

        Cookie viewCookie = null;

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("cookie" + postId)) {
                   viewCookie = cookie;
                }
            }
        }
        if (viewCookie == null) {
            Cookie newCookie = new Cookie("cookie" + postId, String.valueOf(postId));
            response.addCookie(newCookie);
            usedRepository.updatePostView(postId);
        }

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



