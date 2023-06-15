package com.anabada.neighbor.member.service;

import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.member.repository.MemberRepository;
import com.anabada.neighbor.page.Criteria;
import com.anabada.neighbor.used.domain.Post;
import com.anabada.neighbor.used.domain.Product;
import com.anabada.neighbor.used.domain.Used;
import com.anabada.neighbor.used.repository.UsedRepository;
import com.anabada.neighbor.used.service.ImgDownService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService{

    private final UsedRepository usedRepository;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ImgDownService imgDownService;
    private final String uploadDir = "C:\\upload_anabada\\profile\\";
    @Override
    public void save(Member member) {
        member.setMemberPWD(bCryptPasswordEncoder.encode(member.getMemberPWD()));
        member.setProviderId(member.getMemberEmail());
        member.setRole("ROLE_USER");
        memberRepository.save(member);
    }

    @Override
    public List<Used> myWrite(PrincipalDetails principalDetails, Criteria criteria) {
        List<Used> used = new ArrayList<>();
        long memberId = principalDetails.getMember().getMemberId(); // 시큐리티에서 memberId가져오기
        Map<String, Object> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("criteria",criteria);
        String memberName = principalDetails.getMember().getMemberName();
        List<Post> postList = memberRepository.findMyPost(map);//내가 작성한 post가져오기
        for (Post post : postList) {
            Product product = usedRepository.findProduct(post.getPostId());//postId로 product 가져오기
            String categoryName = usedRepository.findCategoryName(product.getCategoryId());//product로 가져온 카테고리id로 category이름가져오기
            Used used1 = Used.builder()
                    .postId(post.getPostId())
                    .memberName(memberName)
                    .title(post.getTitle())
                    .content(post.getContent())
                    .postType(post.getPostType())
                    .postDate(post.getPostDate())
                    .postUpdate(post.getPostUpdate())
                    .postView(post.getPostView())//작성한 글
                    .categoryName(categoryName)//카테고리
                    .build();
            used.add(used1);
        }

        return used;
    }

    @Override
    public int getTotal(long memberId) {//페이징
        return memberRepository.getTotal(memberId);
    }

    @Override
    public Member myInfo(PrincipalDetails principalDetails) {//내정보
        Member member = null;
        long memberId = principalDetails.getMember().getMemberId();
        member = memberRepository.findMyInfo(memberId);
        member.setMyWrite(memberRepository.countMyWrite(memberId));


        return  member;
    }

    @Override
    public String findProfileImg(long memberId) {//프로필사진 이름가져오기
        String profileImg = memberRepository.findProfileImg(memberId);
        return profileImg;
    }

    @Override
    public void downProfileImg(HttpServletResponse response, String profileImg) throws IOException {//프사다운
            imgDownService.downProfileImg(profileImg,response) ;
    }

    @Override
    public List<Used> myWriteFive(long memberId) {//내가 작성한 중고게시글5개만
        List<Used> used = new ArrayList<>();
        List<Post> postList = memberRepository.findMyPostFive(memberId);//내가 작성한 post가져오기
        for (Post post : postList) {
            Product product = usedRepository.findProduct(post.getPostId());//postId로 product 가져오기
            String categoryName = usedRepository.findCategoryName(product.getCategoryId());//product로 가져온 카테고리id로 category이름가져오기
            Used used1 = Used.builder()
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .postType(post.getPostType())
                    .postDate(post.getPostDate())
                    .postUpdate(post.getPostUpdate())
                    .postView(post.getPostView())//작성한 글
                    .categoryName(categoryName)//카테고리
                    .build();
            used.add(used1);
        }
        return used;
    }

    @Override
    public void editInfo(Member member) {
        if(member.getMemberPWD()!=null){//비밀번호가 들어온 경우
            member.setMemberPWD(bCryptPasswordEncoder.encode(member.getMemberPWD()));
            memberRepository.editInfo(member);
        }else{//안 들어온 경우
            memberRepository.editInfoNotPwd(member);
        }
        String a = member.getProfileImg().getOriginalFilename();
        System.out.println("a = " + a);
        boolean b = !member.getProfileImg().getOriginalFilename().equals("") && member.getProfileImg().getOriginalFilename() != null;
        System.out.println("b = " + b);
        if(!member.getProfileImg().getOriginalFilename().equals("") && member.getProfileImg().getOriginalFilename()!=null){//사진 input 했을 때만
            
            try {
                if (Files.exists(Paths.get(uploadDir))) {//폴더가 없으면 만듦.
                    Files.createDirectories(Paths.get(uploadDir));
                }
                MultipartFile file = member.getProfileImg();
                String uuid = UUID.randomUUID().toString();
                String profileImg = uuid+"_"+file.getOriginalFilename();
                
                String filePath = uploadDir+File.separator+profileImg;
                file.transferTo(new File(filePath));

                Map<String, Object> map = new HashMap<>();
                map.put("memberId", member.getMemberId());
                map.put("profileImg",profileImg);
                memberRepository.editProfileImg(map);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public List<Member> findAllMember() {//관리자 모든 멤버 가져오기
        List<Member> member = memberRepository.findAllMember();
        for (Member member1 : member) {
            long total = getTotal(member1.getMemberId());
            member1.setMyWrite(total);//사용자가 작성한 글의 숫자 가져오기
        }
        return member;
    }
}
