package com.anabada.neighbor.member.service;

import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.member.repository.MemberRepository;
import com.anabada.neighbor.page.Criteria;
import com.anabada.neighbor.reply.repository.ReplyRepository;
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
    private final ReplyRepository replyRepository;
    final String uploadDir = "C:\\upload_anabada\\profile\\";

    /**
     * 회원 가입
     */
    @Override
    public void save(Member member) {
        member.setMemberPWD(bCryptPasswordEncoder.encode(member.getMemberPWD()));
        member.setProviderId(member.getMemberEmail());
        member.setRole("ROLE_USER");
        memberRepository.save(member);
    }

    /**
     *내가 작성한 게시글 리스트 (Criteria 로 페이징)
     */
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

    /**
     *페이징을 위한 내가 쓴 글 총 갯수 가져오기
     */
    @Override
    public int getTotal(long memberId) {//페이징
        return memberRepository.countMyWrite(memberId);
    }

    /**
     * member 테이블에서 정보 가져오기
     */
    @Override
    public Member myInfo(PrincipalDetails principalDetails) {//내정보
        Member member = null;
        long memberId = principalDetails.getMember().getMemberId();
        member = memberRepository.findMyInfo(memberId);
        member.setMyWrite(memberRepository.countMyWrite(memberId));
        member.setMyReply(replyRepository.findMyReply(memberId).size());
        member.setMyLikesCount(memberRepository.countMyLikes(memberId));

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


    /**
     * 내가 작성한 중고게시글 5개 .. 나중에 클럽도 포함 해야 할듯 함
     */
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

    /**
     * member테이블
     * 개인정보 수정
     */
    @Override
    public void editInfo(Member member) {
        if(member.getMemberPWD()!=null){//비밀번호가 들어온 경우
            member.setMemberPWD(bCryptPasswordEncoder.encode(member.getMemberPWD()));
            memberRepository.editInfo(member);
        }else{//안 들어온 경우
            memberRepository.editInfoNotPwd(member);
        }

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

    /**
     *관리자페이지에서 모든 member 가져오기
     */
    @Override
    public List<Member> findAllMember(Criteria criteria) {//관리자 모든 멤버 가져오기
        Map<String, Object> map = new HashMap<>();
        map.put("criteria", criteria);
        List<Member> member = memberRepository.findAllMember(map);
        for (Member member1 : member) {
            long total = getTotal(member1.getMemberId());
            member1.setMyWrite(total);//사용자가 작성한 글의 숫자 가져오기
        }
        return member;
    }

    /**
     * 페이징을 위한 멤버의 총 수 가져오기
     * @return 멤버테이블의 count
     */
    @Override
    public int countMember() {
        return memberRepository.countMember();
    }
}
