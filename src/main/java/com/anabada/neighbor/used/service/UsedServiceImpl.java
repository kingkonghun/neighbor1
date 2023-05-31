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
    public List<Used> list() {//글 리스트
        List<Used> usedList = new ArrayList<>(); //리턴할 값
        List<Post> postList = usedRepository.postList(); //post 테이블에서 postType이 'used'인 튜플 가져오기

        for (Post post : postList) {
            Member member = usedRepository.findMember(post.getMemberId()); //post 테이블의 memberId로 member 테이블에서 해당하는 튜플 가져오기
            Product product = usedRepository.findProduct(post.getPostId()); //post 테이블의 postId로 product 테이블에서 해당하는 튜블 가져오기
            String categoryName = usedRepository.findCategoryName(product.getCategoryId()); //product 테이블의 categoryId로 Category 테이블에서 해당하는 categoryName 가져오기

            Used used = Used.builder() //used 객체 생성
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
            usedList.add(used); //리턴할 usedList에 used객체 추가
        }

        return usedList;

    }
    @Override
    public List<Category> categoryList() {//카테고리 리스트
        List<Category> categoryList = usedRepository.categoryList();//리턴할 리스트

        return categoryList;
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
    public Used detail(long postId, HttpServletRequest request, HttpServletResponse response) { //게시물 상세보기
        Post post = usedRepository.findPost(postId); //파라미터로 받은 postId에 해당하는 튜플을 post 테이블에서 가져오기
        Product product = usedRepository.findProduct(postId); //파라미터로 받은 postId에 해당하는 튜플을 product 테이블에서 가져오기
        Member member = usedRepository.findMember(post.getMemberId()); //가져온 post의 memberId로 member 테이블에서 해당하는 튜플 가져오기
        String categoryName = usedRepository.findCategoryName(product.getCategoryId()); //가져온 product의 categoryId로 category 테이블에서 해당하는 categoryName 가져오기

        Cookie[] cookies = request.getCookies(); //쿠키 가져오기

        Cookie viewCookie = null; //

        if (cookies != null && cookies.length > 0) { //가져온 쿠키가 있으면
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("cookie" + postId)) { //해당하는 게시물의 쿠키가 있으면
                   viewCookie = cookie; //viewCookie에 저장
                }
            }
        }
        if (viewCookie == null) { //쿠키가 없으면
            Cookie newCookie = new Cookie("cookie" + postId, String.valueOf(postId)); //해당하는 게시물의 새로운 쿠키 생성
            response.addCookie(newCookie); //쿠키 등록
            usedRepository.updatePostView(postId); //postId로 post 테이블에서 해당하는 튜플의 조회수 증가
        }

        return Used.builder() //게시물의 상세정보 리턴
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



