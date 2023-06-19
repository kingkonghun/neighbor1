package com.anabada.neighbor.used.service;

import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.member.repository.MemberRepository;
import com.anabada.neighbor.used.domain.*;
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
import java.util.*;

@Service
@RequiredArgsConstructor
public class UsedServiceImpl implements UsedService{

    private final UsedRepository usedRepository;
    private final ImgDownService imgDownService;
    private final MemberRepository memberRepository;
    String uploadDir = "C:\\upload_anabada\\";

    @Override
    public List<Used> mainList() {
        List<Post> postList = usedRepository.postList();
        List<Used> usedList = new ArrayList<>();//리스트 담는곳
        for (Post post: postList){
            Product product = usedRepository.findProduct(post.getPostId());//인기있는 제품
            Member member = usedRepository.findMember(post.getMemberId());
            String categoryName = usedRepository.findCategoryName(product.getCategoryId());
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
//                    .profileImg(member.getProfileImg().getOriginalFilename())
                    .score(member.getScore())
                    .memberStatus(member.getMemberStatus())
                    .build();
            usedList.add(used);//리턴할 usedList에 used객체 추가
        }

        return usedList; //usedList 리턴
    }



    @Override
    public List<Used> list(long categoryId, String listType, int num, String search) {//글 리스트
        List<Used> usedList = new ArrayList<>(); //리턴할 값
        List<Product> productList = null;
        if (categoryId != 0) { //파라미터로 받은 categoryId가 0이 아니면
            productList = usedRepository.productCategoryList(categoryId); //product 테이블에서 categoryId가 파라미터로 받은 categoryId랑 같은 튜플 가져오기
        }else { //파라미터로 받은 categoryId가 0이면
            productList = usedRepository.productList(); //product 리스트 가져오기
        }
        for (Product product : productList) {
            Post post = usedRepository.findPost(product.getPostId()); //product 테이블의 postId로 post 테이블에서 해당하는 튜플 가져오기
            Member member = usedRepository.findMember(post.getMemberId()); //post 테이블의 memberId로 member 테이블에서 해당하는 튜플 가져오기
            String categoryName = usedRepository.findCategoryName(product.getCategoryId()); //product 테이블의 categoryId로 Category 테이블에서 해당하는 categoryName 가져오기
            int replyCount = usedRepository.findReplyCount(post.getPostId());
            int likesCount = usedRepository.findLikesCount(post.getPostId());
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
                    .productStatus(product.getProductStatus().equals("y") ? "판매중" : "판매 완료")
                    .categoryId(product.getCategoryId())
                    .memberId(member.getMemberId())
                    .address(member.getAddress())
                    .memberName(member.getMemberName())
//                    .profileImg(member.getProfileImg().getOriginalFilename())
                    .score(member.getScore())
                    .memberStatus(member.getMemberStatus())
                    .replyCount(replyCount)
                    .likesCount(likesCount)
                    .build();
            if (used.getTitle().indexOf(search) != -1 || used.getContent().indexOf(search) != -1) {
                usedList.add(used);//리턴할 usedList에 used객체 추가
            }
        }
        //리스트를 postupdate로 내림차순 정렬
        Comparator<Used> comparator = (use1, use2) -> Long.valueOf(
                use1.getPostUpdate().getTime())
                .compareTo(use2.getPostUpdate().getTime());
        Collections.sort(usedList, comparator.reversed());

        if (listType.equals("similarList")) { //listType이 similarList라면
            return usedList.subList(0, Math.min(usedList.size(), 4)); //usedList에서 앞에 4개만 리턴
        }

        if(num>=usedList.size()){//시작하는값이 usedList보다 크면 아무것도안함
            return null;
        } return usedList.subList(num,Math.min(usedList.size(),num+9));

    }
    @Override
    public List<Category> categoryList() {//카테고리 리스트
        List<Category> categoryList = usedRepository.categoryList();//리턴할 리스트

        return categoryList;
    }

    @Transactional
    @Override
    public void write(Used used, PrincipalDetails principalDetails) {//글쓰기
        used.setMemberId(principalDetails.getMember().getMemberId());
        usedRepository.writePost(used);
        usedRepository.writeProduct(used);
        try {
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
    public void update(Used used, PrincipalDetails principalDetails) throws Exception{//게시글수정
        used.setMemberId(principalDetails.getMember().getMemberId());
        String formImg = used.getFiles().get(0).getOriginalFilename();
        usedRepository.updatePost(used);
        usedRepository.updateProduct(used);

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
            Path uploadDirPath = Path.of(uploadDir+usedRepository.findImgUrl(postId));
            Files.delete(uploadDirPath);
            usedRepository.deleteReply(postId);
            usedRepository.deleteImg(postId);
            usedRepository.deleteProduct(postId);
            usedRepository.deletePost(postId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Used detail(long postId, HttpServletRequest request, HttpServletResponse response, PrincipalDetails principalDetails) { //게시물 상세보기
        Post post = usedRepository.findPost(postId); //파라미터로 받은 postId에 해당하는 튜플을 post 테이블에서 가져오기
        Product product = usedRepository.findProduct(postId); //파라미터로 받은 postId에 해당하는 튜플을 product 테이블에서 가져오기
        Member member = usedRepository.findMember(post.getMemberId()); //가져온 post의 memberId로 member 테이블에서 해당하는 튜플 가져오기
        String categoryName = usedRepository.findCategoryName(product.getCategoryId()); //가져온 product의 categoryId로 category 테이블에서 해당하는 categoryName 가져오기
        int replyCount = usedRepository.findReplyCount(post.getPostId());
        int likesCount = usedRepository.findLikesCount(post.getPostId());
        int likesCheck = 0;
        if (principalDetails != null) {
            likesCheck = usedRepository.likesCheck(Likes.builder()
                    .postId(postId)
                    .memberId(principalDetails.getMember().getMemberId())
                    .build());
        }

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
//                .profileImg(member.getProfileImg().getOriginalFilename())
                .score(member.getScore())
                .memberStatus(member.getMemberStatus())
                .replyCount(replyCount)
                .likesCount(likesCount)
                .likesCheck(likesCheck)
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

    @Override
    public Used likes(long postId, PrincipalDetails principalDetails, int likesCheck) {
        if (likesCheck == 0) {
            usedRepository.likesUp(Likes.builder()
                    .postId(postId)
                    .memberId(principalDetails.getMember().getMemberId())
                    .build());
            likesCheck = 1;
        }else {
            usedRepository.likesDown(Likes.builder()
                    .postId(postId)
                    .memberId(principalDetails.getMember().getMemberId())
                    .build());
            likesCheck = 0;
        }
        return Used.builder()
                .likesCount(usedRepository.findLikesCount(postId))
                .likesCheck(likesCheck)
                .build();
    }

    @Override
    public List<ReportType> reportType() {
        return usedRepository.findAllReportType();
    }

    @Override
    public void report(Report report, PrincipalDetails principalDetails) {
        report.setReporterId(principalDetails.getMember().getMemberId());
        usedRepository.report(report);
    }

    @Override
    public List<PostReport> findAllReport() {
        List<PostReport> postReports = new ArrayList<>();//리턴 그릇
        List<Report> reportList = usedRepository.findAllReport();//신고 테이블 다 긁어옴
        for (Report report : reportList) {
            String reportTypeName = usedRepository.findReportTypeName(report.getReportTypeId());

            long postId=report.getPostId();//요놈으로 포스트갔다가 멤버갔다가 포스트제목과 신고당한사람 이름 가져옴
            Post post=memberRepository.findReportedMember(postId);
            String reportedName = memberRepository.findMemberName(post.getMemberId());

            PostReport postReport = PostReport.builder()
                    .postId(report.getPostId())//신고 당한 게시글
                    .reportTypeName(reportTypeName)//신고 타입..
                    .reporterId(report.getReporterId())//신고자
                    .reporterName(memberRepository.findMemberName(report.getReporterId()))//신고자 이름
                    .reportedName(reportedName)
                    .content(report.getContent())//신고 내용
                    .reportId(report.getReportId())//신고 번호
                    .title(post.getTitle())//신고 당한 게시글 제목
                    .build();
            postReports.add(postReport);

          }
        return postReports;
    }

    @Override
    public List<Used> likePost(long memberId) {
        List<Used> usedList = new ArrayList<>();//리턴그릇
        List<Likes> likesList = usedRepository.findLikePosts(memberId);//좋아요 누른 게시글 긁어오기
        for (Likes likes : likesList) {//좋아요누른 게시글만큼 반복
            long postId = likes.getPostId();//좋아요 누른 게시글id
            Post post = usedRepository.findPost(postId);
            Used used = Used.builder()
                    .postId(postId)
                    .title(post.getTitle())
                    .content(post.getContent())
                    .postView(post.getPostView())
                    .likesCount(usedRepository.findLikesCount(postId))
                    .build();
            /*제목, 내용, 조회수,좋아요 수*/
            usedList.add(used);
        }
        return usedList;
    }


}



